
-- Create dummy data for users
INSERT INTO users (
    id,
    name,
    email,
    role,
    password,
    address,
    phone,
    created_at,
    updated_at
) VALUES (
    '107c8650-93cd-4e13-b3cf-86c80bede1e5',
    'admin',
    'admin@example.com',
    1,
    '$2y$10$Z1QQcS0vhLWc1Y8joNncuONEFdIqqPJ/DwYV2V/pw4Yda4Vbzc5nW',
    '123 Main St, Springfield, IL 62701',
    '+621234567890',
    NOW(),
    NOW()
), (
    '870a4ee5-1097-4329-821b-8fe80a25e5c2',
    'user',
    'user@example.com',
    2,
    '$2y$10$5OJLtq1JYp6oMghdxGSBze6NXhyS6U6U5ofu0MqwqFVqY14OlpWN2',
    '456 Elm St, Springfield, IL 62701',
    '+621234567891',
    NOW(),
    NOW()
);

-- Create function to update books tsv
CREATE OR REPLACE FUNCTION update_books_tsv()
RETURNS TRIGGER AS $$
BEGIN
  NEW.tsv := to_tsvector('english', NEW.title || ' ' || NEW.author || ' ' || NEW.description);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Drop triggers books_tsv_trigger on books if exist
DROP TRIGGER IF EXISTS books_tsv_trigger ON books;

-- Create trigger to update books tsv
CREATE TRIGGER books_tsv_trigger
BEFORE INSERT OR UPDATE ON books
FOR EACH ROW
EXECUTE FUNCTION update_books_tsv();
