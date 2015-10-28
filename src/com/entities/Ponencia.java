/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juandiego
 */
@Entity
@Table(name = "ponencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ponencia.findAll", query = "SELECT p FROM Ponencia p"),
    @NamedQuery(name = "Ponencia.findById", query = "SELECT p FROM Ponencia p WHERE p.id = :id"),
    @NamedQuery(name = "Ponencia.findByNombre", query = "SELECT p FROM Ponencia p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Ponencia.findByAnio", query = "SELECT p FROM Ponencia p WHERE p.anio = :anio")})
public class Ponencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "anio")
    @Temporal(TemporalType.DATE)
    private Date anio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ponenciaId")
    private List<PonenciaAutor> ponenciaAutorList;
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Evento eventoId;
    @JoinColumn(name = "trabajo_investigacion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TrabajoInvestigacion trabajoInvestigacionId;

    public Ponencia() {
    }

    public Ponencia(Integer id) {
        this.id = id;
    }

    public Ponencia(Integer id, String nombre, Date anio) {
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getAnio() {
        return anio;
    }

    public void setAnio(Date anio) {
        this.anio = anio;
    }

    @XmlTransient
    public List<PonenciaAutor> getPonenciaAutorList() {
        return ponenciaAutorList;
    }

    public void setPonenciaAutorList(List<PonenciaAutor> ponenciaAutorList) {
        this.ponenciaAutorList = ponenciaAutorList;
    }

    public Evento getEventoId() {
        return eventoId;
    }

    public void setEventoId(Evento eventoId) {
        this.eventoId = eventoId;
    }

    public TrabajoInvestigacion getTrabajoInvestigacionId() {
        return trabajoInvestigacionId;
    }

    public void setTrabajoInvestigacionId(TrabajoInvestigacion trabajoInvestigacionId) {
        this.trabajoInvestigacionId = trabajoInvestigacionId;
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
        if (!(object instanceof Ponencia)) {
            return false;
        }
        Ponencia other = (Ponencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Ponencia[ id=" + id + " ]";
    }
    
}
