ALTER TABLE documents ADD COLUMN minio_object_name VARCHAR(255);
UPDATE documents SET minio_object_name = file_name WHERE minio_object_name IS NULL;
ALTER TABLE documents ALTER COLUMN minio_object_name SET NOT NULL;
