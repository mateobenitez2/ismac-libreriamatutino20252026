package com.distribuida.service;

import com.distribuida.Service.ClienteService;
import com.distribuida.Service.ClienteServiceImpl;
import com.distribuida.dao.ClienteDAO;
import com.distribuida.model.cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTestUnitaria {

    @Mock
    private ClienteDAO clienteDAO;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private cliente Cliente;

    @BeforeEach
    public void setup(){
        Cliente = new cliente();
        Cliente.setIdCliente(1);
        Cliente.setCedula("1726813684");
        Cliente.setNombre("Mateo");
        Cliente.setApellido("Benitez");
        Cliente.setDireccion("Av. mmv");
        Cliente.setTelefono("0997615005");
        Cliente.setCorreo("mateo@correo.com");
    }

    @Test
    public void findAll(){
        when(clienteDAO.findAll()).thenReturn(List.of(Cliente));
        List<cliente> clientes = clienteService.findAll();

        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        verify(clienteDAO, times(1)).findAll();
    }

    @Test
    public void testfindOne(){
        when(clienteDAO.findById(1)).thenReturn(Optional.ofNullable(Cliente));
        Optional<cliente> Cliente = clienteService.findOne(1);

        assertNotNull(Cliente);
        assertEquals("Mateo", Cliente.orElse(null).getNombre());
    }

    @Test
    public void testFindOneNoExitente(){
        when(clienteDAO.findById(2)).thenReturn(null);
        Optional<cliente> Cliente = clienteService.findOne(2);
        assertNull(Cliente);
    }

    @Test
    public void testSave(){
        when(clienteDAO.save(Cliente)).thenReturn(Cliente);
        cliente clienteGuardado = clienteService.save(Cliente);

        assertNotNull(clienteGuardado);
        assertEquals("Mateo", clienteGuardado.getNombre());
    }

    @Test
    public void testUpdateExistente(){
        cliente clienteActualizado = new cliente();
        clienteActualizado.setCedula("1712345678");
        clienteActualizado.setNombre("Jose");
        clienteActualizado.setApellido("Carro");
        clienteActualizado.setDireccion("Casa12");
        clienteActualizado.setTelefono("0991234567");
        clienteActualizado.setCorreo("Jose@correo.com");

        when(clienteDAO.findById(1)).thenReturn(Optional.ofNullable(Cliente));
        when(clienteDAO.save(any())).thenReturn(clienteActualizado);

        cliente clienteResultado = clienteService.update(1, clienteActualizado);

        assertNotNull(clienteResultado);
        assertEquals("Jose", clienteResultado.getNombre());
        verify(clienteDAO, times(1)).save(Cliente);
    }

    @Test
    public void testUpdateNoExistente(){
        cliente clienteNuevo = new cliente();
        when(clienteDAO.findById(999)).thenReturn(null);
        cliente resultado = clienteService.update(999, clienteNuevo);

        assertNull(resultado);
        verify(clienteDAO, never()).save(any());
    }

    @Test
    public void testDeleteExistente(){
        when(clienteDAO.existsById(1)).thenReturn(true);
        clienteService.delete(1);
        verify(clienteDAO).deleteById(1);
    }

    @Test
    public void testDeleteNoExistente(){
        when(clienteDAO.existsById(999)).thenReturn(false);
        clienteService.delete(999);
        verify(clienteDAO, never()).deleteById(anyInt());
    }

}
