ALTER TABLE menu
ADD INDEX idx_menu_restaurant_active (restaurant_id, is_active);

ALTER TABLE menu_item
ADD INDEX idx_menu_item_menu_available(menu_id, is_available);