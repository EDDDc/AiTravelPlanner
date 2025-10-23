CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    auth_provider VARCHAR(20) NOT NULL,
    avatar_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_settings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    provider VARCHAR(30) NOT NULL,
    key_alias VARCHAR(80) NOT NULL,
    encrypted_key TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_provider UNIQUE (user_id, provider)
);

CREATE TABLE travel_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    title VARCHAR(160) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    budget_total NUMERIC(12,2),
    preferences JSONB,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_travel_plans_user ON travel_plans (user_id);

CREATE TABLE travel_plan_destinations (
    plan_id UUID NOT NULL REFERENCES travel_plans (id) ON DELETE CASCADE,
    destination VARCHAR(160) NOT NULL
);

CREATE INDEX idx_travel_plan_destinations_plan ON travel_plan_destinations (plan_id);

CREATE TABLE day_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plan_id UUID NOT NULL REFERENCES travel_plans (id) ON DELETE CASCADE,
    day_index INTEGER NOT NULL,
    plan_date DATE NOT NULL,
    summary VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_day_plans UNIQUE (plan_id, day_index)
);

CREATE TABLE activities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    day_plan_id UUID NOT NULL REFERENCES day_plans (id) ON DELETE CASCADE,
    type VARCHAR(30) NOT NULL,
    name VARCHAR(180) NOT NULL,
    description TEXT,
    start_time TIME,
    end_time TIME,
    location_lat DOUBLE PRECISION,
    location_lng DOUBLE PRECISION,
    location_address TEXT,
    cost_estimate NUMERIC(12,2),
    metadata JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activities_day_plan ON activities (day_plan_id);

CREATE TABLE expenses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plan_id UUID NOT NULL REFERENCES travel_plans (id) ON DELETE CASCADE,
    category VARCHAR(30) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    method VARCHAR(20) NOT NULL,
    transcript TEXT,
    recorded_at TIMESTAMPTZ NOT NULL,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_expenses_plan ON expenses (plan_id);
CREATE INDEX idx_expenses_recorded_at ON expenses (plan_id, recorded_at);

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plan_id UUID NOT NULL REFERENCES travel_plans (id) ON DELETE CASCADE,
    type VARCHAR(30) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    trigger_time TIMESTAMPTZ,
    payload JSONB,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notifications_plan_status ON notifications (plan_id, status);
