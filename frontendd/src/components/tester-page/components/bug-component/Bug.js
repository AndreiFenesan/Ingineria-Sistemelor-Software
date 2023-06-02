import {Button, Card} from "react-bootstrap";
import "./Bug.css"

function BugInfo({infoName, data}) {
    return <div className={"card-info-container"}>
        <Card.Title>{infoName}</Card.Title>
        <Card.Text>{data}</Card.Text>
    </div>;
}

const Bug = ({id, bugName, description, priority, status, postDate, buttonProps}) => {
    function parseDate() {
        return postDate.substring(0, postDate.indexOf("T"));
    }

    return <div id={"card-entire-body"}>
        <Card id={id} bg={"light"} style={{width: "40vw", height: "20vh"}}>
            <Card.Header style={{fontSize: "1.3rem", display: "flex", justifyContent: "space-between"}}>
                {bugName}
                {buttonProps && <Button onClick={buttonProps.onButtonClick}>{buttonProps.buttonValue}</Button>}
            </Card.Header>
            <Card.Body>
                <div className={"cards-info-container"}>
                    <BugInfo infoName={"Description"} data={description}/>
                    <BugInfo infoName={"Priority and Status"} data={priority + " " + status}/>
                    <BugInfo infoName={"Date of posting"} data={parseDate()}/>
                </div>
            </Card.Body>
        </Card>
    </div>
}

export default Bug