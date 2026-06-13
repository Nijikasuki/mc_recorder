from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):

    llm_api_key: str
    llm_base_url: str = "https://open.bigmodel.cn/api/paas/v4"
    llm_model: str = "glm-4-plus"
    llm_temperature: float = 0.7
    postgres_url: str

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        case_sensitive=False,  # ZHIPU_API_KEY 自动映射到 zhipu_api_key
        extra="ignore",  # .env 里多余的字段忽略 (不报错)
    )

settings = Settings()

