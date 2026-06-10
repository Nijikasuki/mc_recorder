from fastapi import FastAPI

app = FastAPI(
    title="mc-recorder AI Service",
    version="0.1.0",
    description="Python AI microservice (LangChain + LangGraph)",
)

@app.get("/health")
async def health():
    return {"status": "ok", "service": "mc-recorder-ai", "version": "0.1.0"}