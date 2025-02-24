CREATE OR REPLACE FUNCTION update_books_tsv()
RETURNS TRIGGER AS $$
BEGIN
  NEW.tsv := to_tsvector('english', NEW.title || ' ' || NEW.author || ' ' || NEW.description);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS books_tsv_trigger ON books;

CREATE TRIGGER books_tsv_trigger
BEFORE INSERT OR UPDATE ON books
FOR EACH ROW
EXECUTE FUNCTION update_books_tsv();
