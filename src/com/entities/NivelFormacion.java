/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juandiego
 */
@Entity
@Table(name = "nivel_formacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelFormacion.findAll", query = "SELECT n FROM NivelFormacion n"),
    @NamedQuery(name = "NivelFormacion.findByIdEstudiosRealizados", query = "SELECT n FROM NivelFormacion n WHERE n.idEstudiosRealizados = :idEstudiosRealizados"),
    @NamedQuery(name = "NivelFormacion.findByTitulo", query = "SELECT n FROM NivelFormacion n WHERE n.titulo = :titulo"),
    @NamedQuery(name = "NivelFormacion.findByNombreTrabajoGrado", query = "SELECT n FROM NivelFormacion n WHERE n.nombreTrabajoGrado = :nombreTrabajoGrado"),
    @NamedQuery(name = "NivelFormacion.findByAnioInicio", query = "SELECT n FROM NivelFormacion n WHERE n.anioInicio = :anioInicio"),
    @NamedQuery(name = "NivelFormacion.findByAnioFin", query = "SELECT n FROM NivelFormacion n WHERE n.anioFin = :anioFin")})
public class NivelFormacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estudios_realizados")
    private Integer idEstudiosRealizados;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "nombre_trabajo_grado")
    private String nombreTrabajoGrado;
    @Basic(optional = false)
    @Column(name = "anio_inicio")
    @Temporal(TemporalType.DATE)
    private Date anioInicio;
    @Column(name = "anio_fin")
    @Temporal(TemporalType.DATE)
    private Date anioFin;
    @JoinColumn(name = "investigador_identificacion", referencedColumnName = "identificacion")
    @ManyToOne(optional = false)
    private Investigador investigadorIdentificacion;

    public NivelFormacion() {
    }

    public NivelFormacion(Integer idEstudiosRealizados) {
        this.idEstudiosRealizados = idEstudiosRealizados;
    }

    public NivelFormacion(Integer idEstudiosRealizados, String titulo, String nombreTrabajoGrado, Date anioInicio) {
        this.idEstudiosRealizados = idEstudiosRealizados;
        this.titulo = titulo;
        this.nombreTrabajoGrado = nombreTrabajoGrado;
        this.anioInicio = anioInicio;
    }

    public Integer getIdEstudiosRealizados() {
        return idEstudiosRealizados;
    }

    public void setIdEstudiosRealizados(Integer idEstudiosRealizados) {
        this.idEstudiosRealizados = idEstudiosRealizados;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreTrabajoGrado() {
        return nombreTrabajoGrado;
    }

    public void setNombreTrabajoGrado(String nombreTrabajoGrado) {
        this.nombreTrabajoGrado = nombreTrabajoGrado;
    }

    public Date getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Date anioInicio) {
        this.anioInicio = anioInicio;
    }

    public Date getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Date anioFin) {
        this.anioFin = anioFin;
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
        hash += (idEstudiosRealizados != null ? idEstudiosRealizados.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelFormacion)) {
            return false;
        }
        NivelFormacion other = (NivelFormacion) object;
        if ((this.idEstudiosRealizados == null && other.idEstudiosRealizados != null) || (this.idEstudiosRealizados != null && !this.idEstudiosRealizados.equals(other.idEstudiosRealizados))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.NivelFormacion[ idEstudiosRealizados=" + idEstudiosRealizados + " ]";
    }
    
}
