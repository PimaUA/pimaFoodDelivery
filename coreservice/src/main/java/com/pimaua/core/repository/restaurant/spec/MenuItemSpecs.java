package com.pimaua.core.repository.restaurant.spec;


import com.pimaua.core.entity.restaurant.MenuItem;
import org.springframework.data.jpa.domain.Specification;

public class MenuItemSpecs {

    public static Specification<MenuItem> hasMenuId(Integer menuId) {
        return (root, query, cb) ->
                cb.equal(root.get("menu").get("id"), menuId);
    }

    public static Specification<MenuItem> hasNameContaining(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<MenuItem> isAvailable(Boolean available) {
        return (root, query, cb) ->
                cb.equal(root.get("isAvailable"), available);
    }
}
