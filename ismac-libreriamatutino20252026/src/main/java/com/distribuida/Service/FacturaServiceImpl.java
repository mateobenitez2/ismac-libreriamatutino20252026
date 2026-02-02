package com.distribuida.Service;

import com.distribuida.dao.FacturaDAO;
import com.distribuida.model.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService{

    @Autowired
    private FacturaDAO facturaDAO;

    @Override
    public List<Factura> findAll() {
        return facturaDAO.findAll();
    }

    @Override
    public Optional<Factura> findOne(int id) {
        return facturaDAO.findById(id);
    }

    @Override
    public Factura save(Factura factura) {
        return facturaDAO.save(factura);
    }

    @Override
    public Factura update(int id, Factura factura) {
        Optional<Factura> facturaExistente = facturaDAO.findById(id);

        if (facturaExistente == null){
            return null;
        }

        facturaExistente.orElse(null).setNumFactura(factura.getNumFactura());
        facturaExistente.orElse(null).setFecha(factura.getFecha());
        facturaExistente.orElse(null).setTotalNeto(factura.getTotalNeto());
        facturaExistente.orElse(null).setIva(factura.getIva());
        facturaExistente.orElse(null).setTotal(factura.getTotal());
        facturaExistente.orElse(null).setCliente(factura.getCliente());

        return facturaDAO.save(facturaExistente.orElse(null));

    }

    @Override
    public void delete(int id) {

        if (facturaDAO.existsById(id)){
            facturaDAO.deleteById(id);
        }

    }
}
