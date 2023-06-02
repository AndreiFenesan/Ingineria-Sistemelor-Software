import "./bugform.css"
import {Alert, Button, FloatingLabel, Form, FormGroup} from "react-bootstrap";
import {useEffect, useState} from "react";

const BugForm = ({onSuccessfullyAddBug}) => {
    const token = "Bearer " + sessionStorage.getItem("authToken")

    const [bugData, setBugData] = useState({
        bugName: "",
        description: "",
        priority: "LOW"
    })
    const [wasBugAddedSuccessfully, setWasBugAddedSuccessfully] = useState({
        isAddedSuccessfully: null,
        message: ""
    })

    useEffect(() => {
        const timeId = setTimeout(() => {
            setWasBugAddedSuccessfully({isAddedSuccessfully: null, message: ""})
        }, 3000)
        return () => {
            clearTimeout(timeId)
        }
    }, [wasBugAddedSuccessfully.isAddedSuccessfully])
    const getCorrectAlert = () => {
        const alertVariant = wasBugAddedSuccessfully.isAddedSuccessfully ? "success" : "danger"
        const alertText = wasBugAddedSuccessfully.isAddedSuccessfully ?
            "Bug successfully added" : wasBugAddedSuccessfully.message

        return (<Alert style={{width: "60%"}} variant={alertVariant}>{alertText}</Alert>)
    }
    const onBugDataChange = (event) => {
        setBugData(prevState => {
            let eventName = event.target.name;
            if (eventName.length === 0) { //means that the change was on select so we set the name to priority
                eventName = "priority"
            }
            return ({
                ...prevState,
                [eventName]: event.target.value
            })
        })
    }

    const sendBug = async () => {
        const {bugName, description, priority} = bugData
        const response = await fetch("http://localhost:8080/api/v1/bugs", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify({
                name: bugName,
                description: description,
                priority: priority
            })
        })

        if (response.status === 200) {
            console.log(response)
            const bug = await response.json()
            console.log(bug)
            await onSuccessfullyAddBug(bug)

            setWasBugAddedSuccessfully({isAddedSuccessfully: true, message: ""})
        } else {
            console.log("Error")
            const messageError = await response.json()
            setWasBugAddedSuccessfully({isAddedSuccessfully: false, message: messageError.description})
        }
    }
    const onAddBugClicked = async () => {
        await sendBug()
    }
    return <div id={"bug-form-container"}>
        <Form className={"rounded border border-2 p-3"}>
            <FormGroup className={"mb-3"}>
                <FloatingLabel label={"Name"} className={"mb-3"}>
                    <Form.Control name={"bugName"} type={"text"} placeholder={"Name"}
                                  onChange={onBugDataChange}></Form.Control>
                </FloatingLabel>
                <FloatingLabel label={"Description"} className={"mb-3"}>
                    <Form.Control name={"description"} type={"text"} placeholder={"Description"}
                                  onChange={onBugDataChange}></Form.Control>
                </FloatingLabel>
                <Form.Select style={{width: "40%"}} size={"sm"} aria-label={"Default"} onChange={onBugDataChange}>
                    <option value={"LOW"}>Low</option>
                    <option value={"MEDIUM"}>Medium</option>
                    <option value={"HIGH"}>High</option>
                </Form.Select>
            </FormGroup>
            <Button className={"mb-3"} onClick={onAddBugClicked}>Add bug</Button>
            {wasBugAddedSuccessfully.isAddedSuccessfully !== null && getCorrectAlert()}
        </Form>
    </div>
}
export default BugForm