package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
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
public class MenuMapperImpl implements MenuMapper {

    @Override
    public Menu toEntity(MenuRequestDto menuRequestDto) {
        if ( menuRequestDto == null ) {
            return null;
        }

        Menu.MenuBuilder menu = Menu.builder();

        menu.name( menuRequestDto.getName() );
        menu.isActive( menuRequestDto.getIsActive() );
        menu.menuItems( menuItemRequestDtoListToMenuItemList( menuRequestDto.getMenuItems() ) );

        return menu.build();
    }

    @Override
    public MenuResponseDto toDto(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuResponseDto.MenuResponseDtoBuilder menuResponseDto = MenuResponseDto.builder();

        menuResponseDto.id( menu.getId() );
        menuResponseDto.name( menu.getName() );
        menuResponseDto.isActive( menu.getIsActive() );
        menuResponseDto.menuItems( menuItemListToMenuItemResponseDtoList( menu.getMenuItems() ) );
        menuResponseDto.updatedAt( menu.getUpdatedAt() );

        return menuResponseDto.build();
    }

    @Override
    public void updateEntity(Menu menu, MenuRequestDto menuRequestDto) {
        if ( menuRequestDto == null ) {
            return;
        }

        menu.setName( menuRequestDto.getName() );
        menu.setIsActive( menuRequestDto.getIsActive() );
        if ( menu.getMenuItems() != null ) {
            List<MenuItem> list = menuItemRequestDtoListToMenuItemList( menuRequestDto.getMenuItems() );
            if ( list != null ) {
                menu.getMenuItems().clear();
                menu.getMenuItems().addAll( list );
            }
            else {
                menu.setMenuItems( null );
            }
        }
        else {
            List<MenuItem> list = menuItemRequestDtoListToMenuItemList( menuRequestDto.getMenuItems() );
            if ( list != null ) {
                menu.setMenuItems( list );
            }
        }
    }

    @Override
    public List<MenuResponseDto> toListDto(List<Menu> menus) {
        if ( menus == null ) {
            return null;
        }

        List<MenuResponseDto> list = new ArrayList<MenuResponseDto>( menus.size() );
        for ( Menu menu : menus ) {
            list.add( toDto( menu ) );
        }

        return list;
    }

    protected MenuItem menuItemRequestDtoToMenuItem(MenuItemRequestDto menuItemRequestDto) {
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

    protected List<MenuItem> menuItemRequestDtoListToMenuItemList(List<MenuItemRequestDto> list) {
        if ( list == null ) {
            return null;
        }

        List<MenuItem> list1 = new ArrayList<MenuItem>( list.size() );
        for ( MenuItemRequestDto menuItemRequestDto : list ) {
            list1.add( menuItemRequestDtoToMenuItem( menuItemRequestDto ) );
        }

        return list1;
    }

    protected MenuItemResponseDto menuItemToMenuItemResponseDto(MenuItem menuItem) {
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

    protected List<MenuItemResponseDto> menuItemListToMenuItemResponseDtoList(List<MenuItem> list) {
        if ( list == null ) {
            return null;
        }

        List<MenuItemResponseDto> list1 = new ArrayList<MenuItemResponseDto>( list.size() );
        for ( MenuItem menuItem : list ) {
            list1.add( menuItemToMenuItemResponseDto( menuItem ) );
        }

        return list1;
    }
}
