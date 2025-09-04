ALTER TABLE orders
ADD INDEX idx_orders_userid_created(user_id, created_at);