import React, { useState , useEffect} from "react";
import axios from "axios";
import "./UploadJob.css";
import Navbar from "../../components/Navbar/Navbar";
import { useLocation, useNavigate } from "react-router-dom";


const token = localStorage.getItem("token");


const UploadJob = () => {
  const location = useLocation();
const navigate = useNavigate();

const jobId = location.state?.jobId;
const isEditMode = !!jobId;
  const [job, setJob] = useState({
    title: "",
    description: "",
    company: "",
    location: "",
    applyLink: ""
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const userId = localStorage.getItem("userId");
  const userName = localStorage.getItem("username");
useEffect(() => {
  if (isEditMode) {
    fetchJobDetails();
  }
}, [jobId]);

const fetchJobDetails = async () => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/jobs/${jobId}`,
      { headers: { Authorization: `Bearer ${token}` } }
    );

    setJob(response.data); // assumes you already have job state
  } catch (error) {
    alert("Failed to load job details");
  }
};

  const handleChange = (e) => {
    setJob({
      ...job,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
  e.preventDefault();

  const token = localStorage.getItem("token");

  if (!token) {
    alert("Session expired. Please login again.");
    navigate("/login");
    return;
  }

  setLoading(true);

  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  };

  try {
    let response;

    if (isEditMode) {
      response = await axios.put(
        `http://localhost:8080/api/jobs/${jobId}`,
        job,
        config
      );

      alert("Job updated successfully!");
    } else {
      response = await axios.post(
        "http://localhost:8080/api/jobs/create",
        job,
        config
      );

      alert("Job created successfully!");
    }

    navigate("/profile");

  } catch (error) {
    console.error("Job operation error:", error.response || error);

    if (error.response?.status === 401) {
      alert("Unauthorized. Please login again.");
      navigate("/login");
    } else if (error.response?.status === 403) {
      alert("You are not allowed to perform this action.");
    } else {
      alert("Operation failed. Please try again.");
    }

  } finally {
    setLoading(false);
  }
};


return (
    <>
    <Navbar/>
  <div className="uploadContainer">
    <div className="uploadCard">
      <h2 className="uploadTitle">{isEditMode ? "Edit Job" : "Upload Job"}</h2>

      <form onSubmit={handleSubmit} className="uploadForm">

        <input
          type="text"
          name="title"
          placeholder="Job Title"
          value={job.title}
          onChange={handleChange}
          required
          className="uploadInput"
        />

        <textarea
          name="description"
          placeholder="Job Description"
          value={job.description}
          onChange={handleChange}
          required
          className="uploadTextarea"
        />

        <input
          type="text"
          name="company"
          placeholder="Company"
          value={job.company}
          onChange={handleChange}
          required
          className="uploadInput"
        />

        <input
          type="text"
          name="location"
          placeholder="Location"
          value={job.location}
          onChange={handleChange}
          required
          className="uploadInput"
        />

        <input
          type="url"
          name="applyLink"
          placeholder="Official Apply Link (https://...)"
          value={job.applyLink}
          onChange={handleChange}
          required
          className="uploadInput"
        />

        <button type="submit" disabled={loading} className="uploadButton">
        {loading
          ? isEditMode
            ? "Updating..."
            : "Uploading..."
          : isEditMode
            ? "Update Job"
            : "Upload Job"}
      </button>

      </form>

      {message && <p className="uploadMessage">{message}</p>}
    </div>
  </div>
  </>
);
};
export default UploadJob;