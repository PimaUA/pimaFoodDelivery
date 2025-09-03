package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.entity.restaurant.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemMapper {
    MenuItem toEntity(MenuItemRequestDto menuItemRequestDto);

    MenuItemResponseDto toDto(MenuItem menuItem);

    @Mapping(target = "id", ignore = false)
    void updateEntity(@MappingTarget MenuItem menuItem, MenuItemRequestDto menuItemRequestDto);

    default Page<MenuItemResponseDto> toPageDto(Page<MenuItem> menuItems){
        return menuItems.map(this::toDto);
    };
}
