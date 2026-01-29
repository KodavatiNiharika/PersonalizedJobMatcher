import React, {useState} from "react";
import {useNavigate} from "react-router-dom"; 
import axios from "axios";

import './Login.css'
function Login() {
    const [userEmail, setUserEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const db = ["admin", "user"];
    const handleSubmit = async(e) => {
        e.preventDefault();
        if(userEmail === "" || password === "") {
            setError("Enter credentails");
            return;
        }
        try {
            const response = await axios.post("http://localhost:8080/api/users/login", {
                email : userEmail,
                password
            });
            const token = response.data.token;
            if(token) {
                localStorage.setItem("jwt", token);
                alert("Login successful!");
                navigate("/dashboard");
            } else alert("User not found.");
        } catch (err) {
            console.error(err);
            alert("Login failed.");
        }

    };
    return (
        <div className="login-form">
            <h1> Login page </h1>
            <form className="login-form" onSubmit={handleSubmit}>
                <input type="text" className="userEmail" placeholder="Enter email" onChange={(e) => setUserEmail(e.target.value)}></input>
                <input type="password" className="password" placeholder="Enter Password" onChange={(e) => setPassword(e.target.value)}></input>
                <button type="submit" className="login-button">Login</button>
            </form>
            {error && <p style={{color:"white"}}>{error}</p>}
        </div>
    );
}
export default Login;