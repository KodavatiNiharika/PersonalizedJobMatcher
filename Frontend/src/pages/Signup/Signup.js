import React, {use, useState} from "react";
import "./Signup.css"
import axios from "axios";

import {useNavigate} from "react-router-dom"; 
function Signup() {
    const [userName, setUsername] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [ReTypedPassword , setReTypedPassword] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const db = ["admin", "user"];
    const handleSubmit = async(e) => {
        e.preventDefault();
        if(password !== ReTypedPassword) {
            setError("Password mis-match");
            return;
        }
        if(password === "" || userName === "" || userEmail === "") {
            setError("Enter credentials");
            return;
        }
        try {
        const response = await axios.post("http://localhost:8080/api/users/signup", {
            userName,
            email: userEmail,
            password
        });

        console.log("User created:", response.data);
        alert("Signup successful!");
        } catch (err) {
        console.error(err);
        alert("Signup failed. Email may already exist.");
        }
        navigate("/dashboard");
    };
    return (
        <div className="signup-form">
            <h1> Signup page </h1>
            <form className="signup-form" onSubmit={handleSubmit}>
                <input type="text" className="userName" placeholder="Enter username" onChange={(e) => setUsername(e.target.value)}></input>
                <input type="text" className="userEmail" placeholder="Enter Email" onChange={(e) => setUserEmail(e.target.value)}></input>
                <input type="password" className="password" placeholder="Enter password" onChange={(e) => setPassword(e.target.value)}></input>
                <input type="password" className="retype-password" placeholder="Re-Type Password" onChange={(e) => setReTypedPassword(e.target.value)}></input>
                
                <button type="submit" className="signup-button">Signup</button>
            </form>
            {error && <p style={{color:"white"}}>{error}</p>}
        </div>
    );
}
export default Signup;