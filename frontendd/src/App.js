import './App.css';
import {useState, useEffect} from "react";
import "./components/login-page/LoginPage"
import 'bootstrap/dist/css/bootstrap.min.css';
import LoginPage from "./components/login-page/LoginPage";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import TesterPage from "./components/tester-page/TesterPage";
import ProgrammerPage from "./components/programmer-page/programmerPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path={"/login"} element={<LoginPage></LoginPage>}/>
                <Route path={"/tester"} element={<TesterPage></TesterPage>}/>
                <Route path={"/programmer"} element={<ProgrammerPage></ProgrammerPage>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
