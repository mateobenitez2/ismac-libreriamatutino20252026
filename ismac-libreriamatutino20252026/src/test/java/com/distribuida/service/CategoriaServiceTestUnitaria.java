package com.distribuida.service;

import com.distribuida.Service.CategoriaServiceImpl;
import com.distribuida.dao.CategoriaDAO;
import com.distribuida.model.Autor;
import com.distribuida.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTestUnitaria {

    @Mock
    private CategoriaDAO categoriaDAO;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;

    @BeforeEach
    public void setup(){
        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setCategoria("Extra Oficial");
        categoria.setDescripcion("Obra de ficcion");
    }

    @Test
    public void findAll(){
        when(categoriaDAO.findAll()).thenReturn(List.of(categoria));
        List<Categoria> categorias = categoriaService.findAll();

        assertNotNull(categorias);
        assertEquals(1, categorias.size());
        verify(categoriaDAO, times(1)).findAll();
    }

    @Test
    public void testfindOne(){
        when(categoriaDAO.findById(1)).thenReturn(Optional.ofNullable(categoria));
        Optional<Categoria> categoria = categoriaService.findOne(1);

        assertNotNull(categoria);
        assertEquals("Extra Oficial", categoria.orElse(null).getCategoria());
    }

    @Test
    public void testfindOneNoExistente(){
        when(categoriaDAO.findById(2)).thenReturn(null);
        Optional<Categoria> categoria = categoriaService.findOne(2);
        assertNull(categoria);
    }

    @Test
    public void testsave(){
        when(categoriaDAO.save(categoria)).thenReturn(categoria);
        Categoria categoriaGuardada = categoriaService.save(categoria);

        assertNotNull(categoriaGuardada);
        assertEquals("Extra Oficial", categoriaGuardada.getCategoria());
    }

    @Test
    public void testUpdateExistente(){
        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setCategoria("Espacial");
        categoriaActualizada.setDescripcion("Extraterrestres");

        when(categoriaDAO.findById(1)).thenReturn(Optional.ofNullable(categoria));
        when(categoriaDAO.save(any())).thenReturn(categoriaActualizada);

        Categoria categoriaResultado = categoriaService.update(1, categoriaActualizada);

        assertNotNull(categoriaResultado);
        assertEquals("Espacial", categoriaResultado.getCategoria());
        verify(categoriaDAO, times(1)).save(categoria);
    }

    @Test
    public void testUpdateNoExistente(){
        Categoria categoriaNuevo = new Categoria();
        when(categoriaDAO.findById(999)).thenReturn(null);
        Categoria resultado = categoriaService.update(999, categoriaNuevo);

        assertNull(resultado);
        verify(categoriaDAO, never()).save(any());
    }

    @Test
    public void testDeleteExistente(){
        when(categoriaDAO.existsById(1)).thenReturn(true);
        categoriaService.delete(1);
        verify(categoriaDAO).deleteById(1);
    }

    @Test
    public void testDeleteNoExistente(){
        when(categoriaDAO.existsById(999)).thenReturn((false));
        categoriaService.delete(999);
        verify(categoriaDAO, never()).deleteById(anyInt());
    }
}
