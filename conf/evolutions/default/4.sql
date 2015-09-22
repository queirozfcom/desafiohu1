
# --- !Ups

ALTER TABLE availabilities ADD CONSTRAINT uniqueavailabilityforhotel UNIQUE(hotel_id,date);