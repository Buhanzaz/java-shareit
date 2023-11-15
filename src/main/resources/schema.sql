DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id    LONG GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_USER_ID UNIQUE (id),
    CONSTRAINT UQ_USER_EMAIL unique (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          LONG GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(255)                            NOT NULL,
    available   BOOLEAN                                 NOT NULL,
    owner_id    LONG REFERENCES users (id),
    request     BOOLEAN,
    CONSTRAINT PK_ITEM_ID UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id            LONG GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_booking TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_booking   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id       INTEGER REFERENCES items (id),
    booker_id     INTEGER REFERENCES users (id),
    status        VARCHAR(25)                             NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id        LONG GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      TEXT                                    NOT NULL,
    item_id   INTEGER REFERENCES items (id),
    author_id INTEGER REFERENCES users (id),
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL
)
