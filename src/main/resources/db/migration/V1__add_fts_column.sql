ALTER TABLE books ADD COLUMN tsv tsvector;

CREATE INDEX books_fts_idx ON books USING GIN(tsv);

CREATE FUNCTION books_tsvector_update() RETURNS TRIGGER AS $$
BEGIN
  NEW.tsv := to_tsvector('english', NEW.title || ' ' || NEW.author || ' ' || NEW.description);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER books_tsvector_trigger
BEFORE INSERT OR UPDATE ON books
FOR EACH ROW EXECUTE FUNCTION books_tsvector_update();
