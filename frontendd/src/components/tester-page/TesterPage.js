import "./TesterPage.css"
import Bug from "./components/bug-component/Bug";
import BugForm from "./components/bug-component/add-bug-component/BugForm";
import {useEffect, useState} from "react";
import {Form} from "react-bootstrap";

const TesterPage = () => {
    const token = "Bearer " + sessionStorage.getItem("authToken")
    const [bugs, setBugs] = useState(null)
    const [areBugsOrdered, setAreBugsOrdered] = useState(false)

    const getAllBugs = async () => {
        const response = await fetch("http://localhost:8080/api/v1/bugs",
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token
                }
            })
        if (response.status === 200) {
            const data = await response.json()
            console.log(data)
            return data
        }
        return null
    }


    const deleteBug = async (bugId) => {
        const response = await fetch(`http://localhost:8080/api/v1/bugs/${bugId}`,
            {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token
                }

            }
        )
        if (response.status === 200) {
            setBugs(prevState => prevState.filter(bug => bug.id !== bugId))
        }
    }

    useEffect(() => {
        async function fetchData() {
            const bugsFromResponse = await getAllBugs()
            if (bugsFromResponse !== null) {
                if (areBugsOrdered) {
                    setBugs(sortBugsByPriority(bugsFromResponse))
                }
                else{
                    setBugs(bugsFromResponse)
                }
            }
        }

        fetchData()

        const interval = setInterval(async () => {
            await fetchData()
        }, 3000)
        return () => clearInterval(interval)

    }, [areBugsOrdered])

    const sortBugsByPriority = (bugs) => {
        const priorityMap = {
            "HIGH": 3,
            "MEDIUM": 2,
            "LOW": 1
        }
        return bugs.sort((b1, b2) => priorityMap[b2.priority] - priorityMap[b1.priority])
    }

    const addBugToList = (bug) => {
        setBugs(prevState => {
            const bugs = [...prevState]
            bugs.push(bug)
            console.log(bugs)
            return bugs
        })
    }
    const onAreOrderedChecked = () => {
        setAreBugsOrdered(prevState => !prevState)
    }

    return <div id={"tester-container"}>
        <div id={"bugs-container"}>
            {bugs && bugs.map(bug => {
                    const testerUsername = sessionStorage.getItem("username")
                    let bugDeleteBtnProperties = undefined
                    if (testerUsername === bug.bugPosterUsername && bug.status!=="SOLVED") {
                        bugDeleteBtnProperties = {
                            onButtonClick: () => deleteBug(bug.id),
                            buttonValue: "Delete"
                        }
                    }

                    return <Bug id={bug.id} bugName={bug.name} description={bug.description}
                                priority={bug.priority}
                                status={bug.status} postDate={bug.postDate}
                                buttonProps={bugDeleteBtnProperties}
                    >
                    </Bug>
                }
            )}
        </div>
        <div style={{width: "10vw", marginLeft: "10vw"}}>
            <Form>
                <Form.Check label={"Order by priority"} onChange={onAreOrderedChecked}></Form.Check>
            </Form>
        </div>
        <BugForm onSuccessfullyAddBug={addBugToList}></BugForm>
    </div>
}

export default TesterPage