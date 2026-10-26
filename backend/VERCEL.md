# Deploy this backend on Vercel

The `vercel.json` framework setting makes Vercel build `Dockerfile.vercel` as a container function. This matters for an existing Vercel project that was previously deploying the repository as static files. The app listens on the `PORT` environment variable, defaulting to port 80 in the container. The `/api` endpoint returns a small JSON status response; all existing `/api/...` controllers run in this same process.

Set the Vercel project's Root Directory to this backend repository root. Add these environment variables in Vercel for each deployment environment you use:

- `SUPABASE_DB_URL`: PostgreSQL JDBC connection URL for the Supabase session pooler, with `sslmode=require`.
- `JWT_SECRET`: secret key for signing tokens.
- `ALLOWED_ORIGINS`: comma-separated frontend origins allowed by CORS, for example `https://trasexp.vercel.app`.

The optional `DB_SCHEMA`, `DB_POOL_SIZE`, and `JWT_EXPIRATION` variables have defaults in `application-prod.yml`. Redeploy after configuring the variables, then check `/api` and `/actuator/health` on the backend's Vercel domain. A database or environment-variable error during startup will appear in the Vercel function logs.
