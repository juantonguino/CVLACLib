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
@Table(name = "ponencia_autor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PonenciaAutor.findAll", query = "SELECT p FROM PonenciaAutor p"),
    @NamedQuery(name = "PonenciaAutor.findById", query = "SELECT p FROM PonenciaAutor p WHERE p.id = :id")})
public class PonenciaAutor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "investigador_identificacion", referencedColumnName = "identificacion")
    @ManyToOne(optional = false)
    private Investigador investigadorIdentificacion;
    @JoinColumn(name = "ponencia_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ponencia ponenciaId;

    public PonenciaAutor() {
    }

    public PonenciaAutor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Investigador getInvestigadorIdentificacion() {
        return investigadorIdentificacion;
    }

    public void setInvestigadorIdentificacion(Investigador investigadorIdentificacion) {
        this.investigadorIdentificacion = investigadorIdentificacion;
    }

    public Ponencia getPonenciaId() {
        return ponenciaId;
    }

    public void setPonenciaId(Ponencia ponenciaId) {
        this.ponenciaId = ponenciaId;
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
        if (!(object instanceof PonenciaAutor)) {
            return false;
        }
        PonenciaAutor other = (PonenciaAutor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.PonenciaAutor[ id=" + id + " ]";
    }
    
}
