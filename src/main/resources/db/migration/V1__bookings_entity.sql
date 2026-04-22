CREATE TABLE bookings
(
    id          UUID NOT NULL,
    vehicle_id      UUID,
    customer_id UUID,
    start_date  date,
    end_date    date,
    CONSTRAINT pk_bookings PRIMARY KEY (id)
);