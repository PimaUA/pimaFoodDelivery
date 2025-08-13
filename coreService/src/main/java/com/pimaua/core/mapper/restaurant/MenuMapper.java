package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {
    Menu toEntity(MenuRequestDto menuRequestDto);

    MenuResponseDto toDto(Menu menu);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Menu menu, MenuRequestDto menuRequestDto);

    List<MenuResponseDto> toListDto(List<Menu> menus);
}
