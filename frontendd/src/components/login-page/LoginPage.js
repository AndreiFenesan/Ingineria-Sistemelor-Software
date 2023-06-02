import "./loginPage.css"
import {Alert, Button, Form, Row} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";

const LoginPage = () => {
    const [userCredentialsState, setUserCredentialsState] = useState({
        username: "",
        password: ""
    })
    const navigate = useNavigate()
    const [notCredentialsCorrectMessage, setNotCredentialsCorrectMessage] = useState(null)
    const onDataChange = (event) => {
        setUserCredentialsState(prevState => {
            return {...prevState, [event.target.name]: event.target.value}
        })
    }
    const onLoginClicked = async () => {
        const tokenResponse = await fetch("http://localhost:8080/api/v1/authentication/session",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(userCredentialsState)
            }
        )
        if (tokenResponse.status === 200) {
            const data = await tokenResponse.json()
            setNotCredentialsCorrectMessage(null)
            sessionStorage.setItem("authToken", data.token)
            sessionStorage.setItem("username", userCredentialsState.username)
            if (data.role === "TESTER") {
                navigate("/tester")//ATENTIE CAND VA FI SI PROGRAMATOR
            } else if (data.role === "PROGRAMMER") {
                navigate("/programmer")
            }
        } else {
            const data = await tokenResponse.json()
            setNotCredentialsCorrectMessage(data.description)
        }

    }


    return <div id={"login-container"}>
        <div id={"login-form-container"}>
            <Form>
                <Form.Group className={"mb-3"}>
                    <Form.Label>Username</Form.Label>
                    <Form.Control name={"username"} type={"text"} placeholder={"Enter username"}
                                  onChange={onDataChange}>
                    </Form.Control>
                </Form.Group>
                <Form.Group className={"mb-3"}>
                    <Form.Label>Password</Form.Label>
                    <Form.Control name={"password"} type={"password"} placeholder={"Enter password"}
                                  onChange={onDataChange}></Form.Control>
                </Form.Group>
                <Row>
                    <Button onClick={onLoginClicked} variant="primary">
                        Login
                    </Button>
                </Row>
                {notCredentialsCorrectMessage && <Alert variant={"danger"}> {notCredentialsCorrectMessage}</Alert>}
            </Form>
        </div>
    </div>
}
export default LoginPage