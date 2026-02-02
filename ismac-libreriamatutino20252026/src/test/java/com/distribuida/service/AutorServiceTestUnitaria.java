package com.distribuida.service;


import com.distribuida.Service.AutorServiceImpl;
import com.distribuida.dao.AutorDAO;
import com.distribuida.model.Autor;
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
public class AutorServiceTestUnitaria {

    @Mock
    private AutorDAO autorDAO;

    @InjectMocks
    private AutorServiceImpl autorService;

    private Autor autor;

    @BeforeEach
    public void setup(){
        autor = new Autor();
        autor.setIdAutor(1);
        autor.setNombre("Mateo");
        autor.setApellido("Benitez");
        autor.setPais("Ecuador");
        autor.setDireccion("Av.mmv");
        autor.setTelefono("0997615005");
        autor.setCorreo("mateo@hotmail.com");
    }

    @Test
    public void findAll(){
        when(autorDAO.findAll()).thenReturn(List.of(autor));
        List<Autor> autores = autorService.findAll();

        assertNotNull(autores);
        assertEquals(1, autores.size());
        verify(autorDAO, times(1)).findAll();
    }

    @Test
    public void testfindOne(){
        when(autorDAO.findById(1)).thenReturn(Optional.ofNullable(autor));
        Optional<Autor> autor = autorService.findOne(1);

        assertNotNull(autor);
        assertEquals("Mateo", autor.orElse(null).getNombre());
    }

    @Test
    public void testfindOneNoExistente(){
        when(autorDAO.findById(2)).thenReturn(null);
        Optional<Autor> autor = autorService.findOne(2);
        assertNull(autor);
    }

    @Test
    public void testsave(){
        when(autorDAO.save(autor)).thenReturn(autor);
        Autor autorGuardado = autorService.save(autor);

        assertNotNull(autorGuardado);
        assertEquals("Mateo", autorGuardado.getNombre());
    }

    @Test
    public void testUpdateExistente(){
        Autor autorActualizado = new Autor();
        autorActualizado.setNombre("Axel");
        autorActualizado.setApellido("Herrera");
        autorActualizado.setPais("Ecuador");
        autorActualizado.setDireccion("Av.mmv");
        autorActualizado.setTelefono("0997625005");
        autorActualizado.setCorreo("axel@correo.com");

        when(autorDAO.findById(1)).thenReturn(Optional.ofNullable(autor));
        when(autorDAO.save(any())).thenReturn(autorActualizado);

        Autor autorResultado = autorService.update(1, autorActualizado);

        assertNotNull(autorResultado);
        assertEquals("Axel", autorResultado.getNombre());
        verify(autorDAO, times(1)).save(autor);
    }

    @Test
    public void testUpdateNoExistente(){
        Autor autorNuevo = new Autor();
        when(autorDAO.findById(999)).thenReturn(null);
        Autor resultado = autorService.update(999, autorNuevo);

        assertNull(resultado);
        verify(autorDAO, never()).save(any());
    }

    @Test
    public void testDeleteExistente(){
        when(autorDAO.existsById(1)).thenReturn(true);
        autorService.delete(1);
        verify(autorDAO).deleteById(1);
    }

    @Test
    public void testDeleteNoExistente(){
        when(autorDAO.existsById(999)).thenReturn((false));
        autorService.delete(999);
        verify(autorDAO, never()).deleteById(anyInt());
    }
}
