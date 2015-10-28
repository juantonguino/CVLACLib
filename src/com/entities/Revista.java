/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juandiego
 */
@Entity
@Table(name = "revista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Revista.findAll", query = "SELECT r FROM Revista r"),
    @NamedQuery(name = "Revista.findByNombre", query = "SELECT r FROM Revista r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Revista.findByIndexada", query = "SELECT r FROM Revista r WHERE r.indexada = :indexada"),
    @NamedQuery(name = "Revista.findByCategorizacion", query = "SELECT r FROM Revista r WHERE r.categorizacion = :categorizacion")})
public class Revista implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "indexada")
    private boolean indexada;
    @Column(name = "categorizacion")
    private String categorizacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "revistaNombre")
    private List<Articulo> articuloList;

    public Revista() {
    }

    public Revista(String nombre) {
        this.nombre = nombre;
    }

    public Revista(String nombre, boolean indexada) {
        this.nombre = nombre;
        this.indexada = indexada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getIndexada() {
        return indexada;
    }

    public void setIndexada(boolean indexada) {
        this.indexada = indexada;
    }

    public String getCategorizacion() {
        return categorizacion;
    }

    public void setCategorizacion(String categorizacion) {
        this.categorizacion = categorizacion;
    }

    @XmlTransient
    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Revista)) {
            return false;
        }
        Revista other = (Revista) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Revista[ nombre=" + nombre + " ]";
    }
    
}
