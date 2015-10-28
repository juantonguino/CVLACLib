/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juandiego
 */
@Entity
@Table(name = "articulo_autor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ArticuloAutor.findAll", query = "SELECT a FROM ArticuloAutor a"),
    @NamedQuery(name = "ArticuloAutor.findById", query = "SELECT a FROM ArticuloAutor a WHERE a.id = :id")})
public class ArticuloAutor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "articulo_id_articulo", referencedColumnName = "id_articulo")
    @ManyToOne(optional = false)
    private Articulo articuloIdArticulo;
    @JoinColumn(name = "investigador_identificacion", referencedColumnName = "identificacion")
    @ManyToOne(optional = false)
    private Investigador investigadorIdentificacion;

    public ArticuloAutor() {
    }

    public ArticuloAutor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Articulo getArticuloIdArticulo() {
        return articuloIdArticulo;
    }

    public void setArticuloIdArticulo(Articulo articuloIdArticulo) {
        this.articuloIdArticulo = articuloIdArticulo;
    }

    public Investigador getInvestigadorIdentificacion() {
        return investigadorIdentificacion;
    }

    public void setInvestigadorIdentificacion(Investigador investigadorIdentificacion) {
        this.investigadorIdentificacion = investigadorIdentificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArticuloAutor)) {
            return false;
        }
        ArticuloAutor other = (ArticuloAutor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.ArticuloAutor[ id=" + id + " ]";
    }
    
}
