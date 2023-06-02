import "./programmerPage.css"
import {useEffect, useState} from "react";
import Bug from "../tester-page/components/bug-component/Bug";
import {Alert, Form} from "react-bootstrap";

const computeUri = (isUnresolvedBugs) => {
    if (isUnresolvedBugs) {
        return "http://localhost:8080/api/v1/bugs?status=UNSOLVED"
    } else return `http://localhost:8080/api/v1/bugs`
}

const ProgrammerPage = () => {
    const token = "Bearer " + sessionStorage.getItem("authToken")
    const [areBugsOrdered, setAreBugsOrdered] = useState(false)
    const [unresolvedBugs, setUnresolvedBugs] = useState(null)
    const [assignedBugs, setAssignedBugs] = useState(null)
    const [errorMessage, setErrorMessage] = useState(null)


    const sortBugsByPriority = (bugs) => {
        const priorityMap = {
            "HIGH": 3,
            "MEDIUM": 2,
            "LOW": 1
        }
        return bugs.sort((b1, b2) => priorityMap[b2.priority] - priorityMap[b1.priority])
    }

    const removeBugWithIdFromUnresolvedBug = (bugId) => {
        setUnresolvedBugs(prevState => {
            return prevState.filter(bug => bug.id !== bugId)
        })
    }

    const removeBugWithIdFromAssignedBug = (bugId) => {
        setAssignedBugs(prevState => {
            return prevState.filter(bug => bug.id !== bugId)
        })
    }
    const assignBugToProgrammer = async (bugId) => {
        const response = await fetch(`http://localhost:8080/api/v1/bugs/unsolved/${bugId}`,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token
                }
            })
        if (response.status === 200) {
            const updatedBug = await response.json()

            await removeBugWithIdFromUnresolvedBug(bugId)

            setAssignedBugs(prevState => {
                const newBugs = [...prevState]
                newBugs.push(updatedBug)
                return newBugs
            })
        } else {
            const error = await response.json()
            setErrorMessage(error.description)
        }
    }

    const getAllBugs = async (isUnresolvedBugs = true) => {
        const uri = computeUri(isUnresolvedBugs)

        const response = await fetch(uri,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token
                }
            })

        if (response.status === 200) {
            const data = await response.json()
            return data
        }
        return null
    }

    const markBugAsSolved = async (bugId) => {
        const response = await fetch(`http://localhost:8080/api/v1/bugs/in-progress/${bugId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token
                }
            }
        )
        if (response.status === 200) {
            removeBugWithIdFromAssignedBug(bugId)
        } else {
            const error = await response.json()
            setErrorMessage(error.description)
        }
    }

    useEffect(() => {
        async function fetchUnresolvedBugs() {
            const bugsFromResponse = await getAllBugs()
            if (bugsFromResponse !== null) {
                if (areBugsOrdered) {
                    setUnresolvedBugs(sortBugsByPriority(bugsFromResponse))
                } else {
                    setUnresolvedBugs(bugsFromResponse)
                }
            }
        }

        async function fetchAssignedBugs() {
            const bugsFromResponse = await getAllBugs(false)
            if (bugsFromResponse !== null) {
                setAssignedBugs(bugsFromResponse)
            }
        }

        fetchUnresolvedBugs()
        fetchAssignedBugs()

        const interval = setInterval(async () => {
            await fetchUnresolvedBugs()
            await fetchAssignedBugs()

        }, 3000)
        return () => clearInterval(interval)

    }, [areBugsOrdered])

    useEffect(() => {
        const timeOut = setTimeout(() => {
            setErrorMessage(null)
        }, 3000)
        return () => clearTimeout(timeOut)
    }, [errorMessage])

    const onAreOrderedChecked = () => {
        setAreBugsOrdered(prevState => !prevState)
    }

    return <div id={"programmer-container"}>
        <div id={"bugs-view"}>
            <div className={"bugs-container"}>
                {unresolvedBugs && unresolvedBugs.map(bug =>
                    <Bug key={bug.id} id={bug.id} bugName={bug.name} description={bug.description}
                         priority={bug.priority}
                         status={bug.status}
                         postDate={bug.postDate}
                         buttonProps={{
                             onButtonClick: () => assignBugToProgrammer(bug.id),
                             buttonValue: "Mark for solving"
                         }}
                    >
                    </Bug>
                )}
            </div>
            <div className={"bugs-container"}>
                {assignedBugs && assignedBugs.map(bug =>
                    <Bug key={bug.id} id={bug.id} bugName={bug.name} description={bug.description}
                         priority={bug.priority}
                         status={bug.status} postDate={bug.postDate}
                         buttonProps={{
                             onButtonClick: () => markBugAsSolved(bug.id),
                             buttonValue: "Resolved"
                         }}
                    >
                    </Bug>
                )}
            </div>
        </div>
        <div style={{width: "10vw", marginLeft: "10vw"}}>
            <Form>
                <Form.Check label={"Order by priority"} onChange={onAreOrderedChecked}></Form.Check>
            </Form>
        </div>
        {errorMessage && <div id={"error-container"}><Alert style={{width: "40vw", height: "100%"}}
                                                            variant={"danger"}>{errorMessage}</Alert></div>}
    </div>
}
export default ProgrammerPage