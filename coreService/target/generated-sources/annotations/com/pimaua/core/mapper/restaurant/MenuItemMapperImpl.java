package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.entity.restaurant.MenuItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T12:41:27+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MenuItemMapperImpl implements MenuItemMapper {

    @Override
    public MenuItem toEntity(MenuItemRequestDto menuItemRequestDto) {
        if ( menuItemRequestDto == null ) {
            return null;
        }

        MenuItem.MenuItemBuilder menuItem = MenuItem.builder();

        menuItem.id( menuItemRequestDto.getId() );
        menuItem.name( menuItemRequestDto.getName() );
        menuItem.description( menuItemRequestDto.getDescription() );
        menuItem.price( menuItemRequestDto.getPrice() );
        menuItem.isAvailable( menuItemRequestDto.getIsAvailable() );

        return menuItem.build();
    }

    @Override
    public MenuItemResponseDto toDto(MenuItem menuItem) {
        if ( menuItem == null ) {
            return null;
        }

        MenuItemResponseDto.MenuItemResponseDtoBuilder menuItemResponseDto = MenuItemResponseDto.builder();

        menuItemResponseDto.id( menuItem.getId() );
        menuItemResponseDto.name( menuItem.getName() );
        menuItemResponseDto.description( menuItem.getDescription() );
        menuItemResponseDto.price( menuItem.getPrice() );
        menuItemResponseDto.isAvailable( menuItem.getIsAvailable() );
        menuItemResponseDto.updatedAt( menuItem.getUpdatedAt() );

        return menuItemResponseDto.build();
    }

    @Override
    public void updateEntity(MenuItem menuItem, MenuItemRequestDto menuItemRequestDto) {
        if ( menuItemRequestDto == null ) {
            return;
        }

        menuItem.setId( menuItemRequestDto.getId() );
        menuItem.setName( menuItemRequestDto.getName() );
        menuItem.setDescription( menuItemRequestDto.getDescription() );
        menuItem.setPrice( menuItemRequestDto.getPrice() );
        menuItem.setIsAvailable( menuItemRequestDto.getIsAvailable() );
    }

    @Override
    public List<MenuItemResponseDto> toListDto(List<MenuItem> menuItems) {
        if ( menuItems == null ) {
            return null;
        }

        List<MenuItemResponseDto> list = new ArrayList<MenuItemResponseDto>( menuItems.size() );
        for ( MenuItem menuItem : menuItems ) {
            list.add( toDto( menuItem ) );
        }

        return list;
    }
}
