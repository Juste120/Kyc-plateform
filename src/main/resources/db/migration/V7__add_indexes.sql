CREATE INDEX idx_documents_customer_id ON documents(customer_id);
CREATE INDEX idx_verifications_customer_id ON verifications(customer_id);
CREATE INDEX idx_notifications_customer_id ON notifications(customer_id);
CREATE INDEX idx_notifications_customer_email ON notifications(customer_email);
CREATE INDEX idx_verifications_status ON verifications(status);
CREATE INDEX idx_documents_document_type ON documents(document_type);
