from pydantic import BaseModel
from typing import List

class Resume(BaseModel):
    userEmail: str
    fullText: str

class ATSRequest(BaseModel):
    description: str
    resumes: List[Resume]