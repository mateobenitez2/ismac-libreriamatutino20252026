package com.distribuida.service;

import com.distribuida.Service.LibroServiceImpl;
import com.distribuida.dao.LibroDAO;
import com.distribuida.model.Autor;
import com.distribuida.model.Categoria;
import com.distribuida.model.FacturaDetalle;
import com.distribuida.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTestUnitaria {

    @Mock
    private LibroDAO libroDAO;

    @InjectMocks
    private LibroServiceImpl libroService;

    private Libro libro;
    private Categoria categoria;
    private Autor autor;

    @BeforeEach
    public void setUp(){
        libro = new Libro();
        libro.setIdLibro(1);
        libro.setTitulo("Titulo");
        libro.setEditorial("Si");
        libro.setNumPaginas(700);
        libro.setEdicion("4th");
        libro.setIdioma("Español");
        libro.setFechaPublicacion(new Date());
        libro.setDescripcion("No");
        libro.setTipoPasta("Dura");
        libro.setIsbn("Nose");
        libro.setNumEjemplares(5);
        libro.setPortada("Blanca");
        libro.setPresentacion("Fisica");
        libro.setPrecio(15);
        libro.setCategoria(categoria);
        libro.setAutor(autor);

        autor = new Autor();
        autor.setIdAutor(1);
        autor.setNombre("Mateo");
        autor.setApellido("Benitez");
        autor.setPais("Ecuador");
        autor.setDireccion("Av.mmv");
        autor.setTelefono("0997615005");
        autor.setCorreo("mateo@hotmail.com");

        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setCategoria("Extra Oficial");
        categoria.setDescripcion("Obra de ficcion");
    }

    @Test
    public void findAll() {
        when(libroDAO.findAll()).thenReturn(List.of(libro));

        List<Libro> libros = libroService.findAll();

        assertNotNull(libros);
        assertEquals(1, libros.size());
        verify(libroDAO, times(1)).findAll();
    }

    @Test
    public void findOneExistente() {
        when(libroDAO.findById(1)).thenReturn(Optional.of(libro));

        Optional<Libro> resultado = libroService.findOne(1);

        assertNotNull(resultado);
        assertEquals("Titulo", resultado.orElse(null).getTitulo());
    }

    @Test
    public void findOneNoExistente() {
        when(libroDAO.findById(2)).thenReturn(null);

        Optional<Libro> resultado = libroService.findOne(2);

        assertNull(resultado);
    }

    @Test
    public void save(){
        when(libroDAO.save(libro)).thenReturn(libro);

        Libro libroGuardado = libroService.save(libro);

        assertNotNull(libroGuardado);
        assertEquals("Titulo", libroGuardado.getTitulo());
    }

    @Test
    public void testUpdateExistente(){
        Libro libroActualizado = new Libro();
        libroActualizado.setTitulo("Holo");
        libroActualizado.setEditorial("Live");
        libroActualizado.setNumPaginas(500);
        libroActualizado.setEdicion("Primera");
        libroActualizado.setIdioma("Ingles");
        libroActualizado.setFechaPublicacion(new Date());
        libroActualizado.setDescripcion("Goty");
        libroActualizado.setTipoPasta("Suave");
        libroActualizado.setIsbn("2026");
        libroActualizado.setNumPaginas(1000);
        libroActualizado.setPortada("FWMC");
        libroActualizado.setPresentacion("Virtual");
        libroActualizado.setPrecio(1000);

        when(libroDAO.findById(1)).thenReturn(Optional.of(libro));
        when(libroDAO.save(any())).thenReturn(libroActualizado);

        Libro resultado = libroService.update(1, libroActualizado);

        assertNotNull(resultado);
        assertEquals("Holo", resultado.getTitulo());
        verify(libroDAO, times(1)).save(libro);
    }

    @Test
    public void updateNoExistente() {
        Libro nuevo = new Libro();

        when(libroDAO.findById(999)).thenReturn(null);

        Libro resultado = libroService.update(999, nuevo);

        assertNull(resultado);
        verify(libroDAO, never()).save(any());
    }

    @Test
    public void deleteExistente() {
        when(libroDAO.existsById(1)).thenReturn(true);

        libroService.delete(1);

        verify(libroDAO).deleteById(1);
    }

    @Test
    public void deleteNoExistente() {
        when(libroDAO.existsById(999)).thenReturn(false);

        libroService.delete(999);

        verify(libroDAO, never()).deleteById(anyInt());
    }
}
