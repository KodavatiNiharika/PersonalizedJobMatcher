import { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../../components/Navbar/Navbar";
import "./Dashboard.css"; // import the CSS

function Dashboard() {
  const [jobs, setJobs] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");

    axios.get("http://localhost:8080/dashboard", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(res => setJobs(res.data))
    .catch(err => console.error(err));
  }, []);

  return (
    <div>
      <Navbar />
      <div className="dashboard-container">
        <h2>Jobs With ATS Score &gt; 40</h2>
        <div className="jobs-list">
          {jobs.map((job) => (
            <div className="job-card" key={job.id}>
              <div className="job-info">
                <h3>{job.title}</h3>

                <div className="info-row">
                  <span className="label">Company:</span>
                  <span className="value">{job.company}</span>
                </div>

                <div className="info-row">
                  <span className="label">Location:</span>
                  <span className="value">{job.location}</span>
                </div>

                <div className="info-row description">
                  <label>DESCRIPTION:</label>
                  <span>{job.description}</span>
                </div>
              </div>

              <div className="job-action">
                <a href={job.applyLink} target="_blank" rel="noopener noreferrer">
                  <button className="apply-btn">Apply</button>
                </a>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;