from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
from scoring import calculate_ats_score
from schemas import ATSRequest, Resume
app = FastAPI(
    title="ATS Scoring Microservice",
    description="Hybrid ATS scoring using Exact, Phrase and Semantic matching",
    version="1.0.0"
)


# ----------------------------
# Health Check (important)
# ----------------------------
@app.get("/")
def health_check():
    return {"status": "ATS Service Running"}


# ----------------------------
# ATS Score Endpoint
# ----------------------------

@app.post("/ats-score")
def calculate_scores(request: ATSRequest):

    results = []

    for resume in request.resumes:
        score_data = calculate_ats_score(
            resume.fullText,      # matches Java
            request.description   # matches Java
        )

        results.append({
            "userEmail": resume.userEmail,
            "score": score_data["final_score"]
        })

    return results