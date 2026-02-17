import React, { useState, useEffect, useRef } from "react";
import "./Profile.css";
import { createPortal } from "react-dom";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/Navbar/Navbar";
import axios from "axios";

const Profile = () => {
  const navigate = useNavigate();
  const fileInputRef = useRef(null);

  const [resume, setResume] = useState(null); // { id, fileName }
  const [resumeUrl, setResumeUrl] = useState("");
  const [isResumeOpen, setIsResumeOpen] = useState(false);
  const [userJobs, setUserJobs] = useState([]);

  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");
  const userName = localStorage.getItem("username");

  /* ================= FETCH DATA ================= */

  useEffect(() => {
    fetchResume();
    fetchUserJobs();
  }, []);

  const handleDeleteJob = async (jobId) => {
  const confirmDelete = window.confirm(
    "Are you sure you want to delete this job?"
  );

  if (!confirmDelete) return;

  try {
    await axios.delete(
      `http://localhost:8080/api/jobs/${jobId}`,
      { headers: { Authorization: `Bearer ${token}` } }
    );

    // remove deleted job from UI instantly
    setUserJobs((prevJobs) =>
      prevJobs.filter((job) => job.id !== jobId)
    );

    alert("Job deleted successfully!");
  } catch (error) {
    console.error("Delete failed", error);
    alert("Failed to delete job!");
  }
};


  const fetchResume = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/resume/user/${userId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      setResume(response.data);
    } catch {
      setResume(null);
    }
  };

  const fetchUserJobs = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/jobs/my-jobs`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      console.log(response);

      setUserJobs(response.data);
    } catch (error) {
      console.error("Failed to fetch jobs", error);
    }
  };

  /* ================= RESUME ACTIONS ================= */

  const handleUploadClick = () => {
    fileInputRef.current.click();
  };

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("userId", userId);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/resume/upload",
        formData,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      setResume(response.data);
      alert("Resume uploaded successfully!");
    } catch (error) {
      alert("Upload failed!");
    }
  };

  const handleViewResume = async () => {
    if (!resume) return;

    try {
      const response = await axios.get(
        `http://localhost:8080/api/resume/view/${resume.id}`,
        {
          responseType: "blob",
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      const file = new Blob([response.data], { type: "application/pdf" });
      setResumeUrl(URL.createObjectURL(file));
      setIsResumeOpen(true);
    } catch {
      alert("Failed to load resume");
    }
  };

  const handleDeleteResume = async () => {
    if (!resume) return;

    const confirmDelete = window.confirm(
      "Are you sure you want to delete your resume?"
    );
    if (!confirmDelete) return;

    try {
      await axios.delete(
        `http://localhost:8080/api/resume/${resume.id}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      setResume(null);
      setIsResumeOpen(false);
      alert("Resume deleted successfully!");
    } catch {
      alert("Failed to delete resume!");
    }
  };

  /* ================= JOB ACTIONS ================= */

  const handleEditJob = (jobId) => {
  navigate("/jobupload", { state: { jobId } });
};


  /* ================= UI ================= */

  return (
    <>
    {!isResumeOpen && <Navbar />}


      <div className="profile-page">

        {/* ===== HEADER ===== */}
        <div className="profile-top">
          <div className="profile-identity">
            <div className="avatar-lg">
              {userName ? userName.charAt(0).toUpperCase() : "U"}
            </div>
            <div>
              <h1>{userName}</h1>
            </div>
          </div>
        </div>

        <div className="profile-stack">

          {/* ===== RESUME SECTION ===== */}
          <div className="panel-card horizontal-card">

            <div className="card-left">
              <input
                type="file"
                accept=".pdf,.doc,.docx"
                ref={fileInputRef}
                onChange={handleFileChange}
                hidden
              />

              {resume ? (
                <div className="resume-display">
                  <span>📄</span>
                  <span>{resume.fileName}</span>
                </div>
              ) : (
                <p className="empty-text">
                  You haven't uploaded a resume yet.
                </p>
              )}
            </div>

            <div className="card-right">
              <button className="btn primary" onClick={handleUploadClick}>
                {resume ? "Upload New Version" : "Upload Resume"}
              </button>

              {resume && (
                <>
                  <button className="btn secondary" onClick={handleViewResume}>
                    View
                  </button>
                  <button className="btn danger" onClick={handleDeleteResume}>
                    Delete
                  </button>
                </>
              )}
            </div>
          </div>

          {/* ===== USER JOBS ===== */}
          <div className="panel-card jobs-card">

            <h2 className="section-title">Your Uploaded Jobs</h2>

            {userJobs.length === 0 ? (
              <p className="empty-text">
                You haven't uploaded any jobs yet.
              </p>
            ) : (
              <div className="jobs-list">
                {userJobs.map((job) => (
                  <div key={job.id} className="job-item">
                    <div>
                      <h3>{job.title}</h3>
                      <p className="job-meta">
                        {job.companyName} • {job.location}
                      </p>
                    </div>

                    <div style={{ display: "flex", gap: "10px" }}>
                      <button
                        className="btn secondary"
                        onClick={() => handleEditJob(job.id)}
                      >
                        Edit
                      </button>

                      <button
                        className="btn danger"
                        onClick={() => handleDeleteJob(job.id)}
                      >
                        Delete
                      </button>
                    </div>

                  </div>
                ))}
              </div>
            )}
          </div>

        </div>
      </div>

      {/* ===== RESUME MODAL ===== */}
      {isResumeOpen &&
        createPortal(
          <div className="resume-modal">
            <button
              className="close-btn"
              onClick={() => setIsResumeOpen(false)}
            >
              ×
            </button>
            <iframe
              src={resumeUrl}
              title="Resume"
              className="resume-frame"
            />
          </div>,
          document.body
        )}
    </>
  );
};

export default Profile;
