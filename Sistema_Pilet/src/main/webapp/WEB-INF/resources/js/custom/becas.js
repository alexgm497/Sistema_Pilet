/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    $.fn.initBootTable = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            
            if(this.id == "TablRegi"){
            consObje([{name : 'codiObjePara', value : row.id.trim()}]); 
            }
            
            if(this.id == "TablDetalle"){
            consObjeDetalle([{name : 'codiObjePara', value : row.idTipoBeca.trim()}]); 
            }
            if(this.id == "TablDocu"){
            consObjeDocu([{name : 'codiObjePara', value : row.idDocu.trim()}]); 
            }
            if(this.id == "TablSegu"){
            consSeguObje([{name : 'codiObjePara', value : row.idSegu.trim()}]); 
            }
            if(this.id == "TablRegiHist"){
            consObjeDocu([{name : 'codiObjePara', value : row.idDocu.trim()}]); 
            }
            
        });
        return false;
    };
    $.fn.initDatePick = function() {
        $(this).datepicker({
            format: "dd/mm/yyyy",
            language: "es",
            orientation: "top auto",
            autoclose: true,
            todayHighlight: true
        }).on('show.bs.modal', function(e) {
            // Quitar el conflicto con bootstrap modal "show.bs.modal"
            e.stopPropagation();
        }).on('hide.bs.modal', function(e) {
            // Quitar el conflicto con bootstrap modal "hide.bs.modal"
            e.stopPropagation();
        });
    };
    $('#ModaForm').on('show.bs.modal', function() {
        
            });
    $('#ModaDocuForm').on('show.bs.modal', function() {
       
    });
    $('#ModaFormSegu').on('show.bs.modal', function() {
         
    });
    
    $('#ModaForm').on('hide.bs.modal', function() {
        $("#TablDetalle").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro detalle")
    });
    
    $('#ModaDocuForm').on('hide.bs.modal', function() {
        $("#TablDocu").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro documentos")
    });
     $('#FormRegiSegu').on('hide.bs.modal', function() {
        $("#TablSegu").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro seguimientos")
    });
    $('#FormRegiDocu').on('hide.bs.modal', function() {
        $("#TablDocu").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro seguimientos")
    });
    INIT_OBJE();
});

function INIT_OBJE()
{
    $("#TablRegi").initBootTable();
    $("#TablDetalle").initBootTable();
    $("#TablDocu").initBootTable();
    $("#TablSegu").initBootTable();
    $(".select").selectpicker();    
    INIT_OBJE_MODA();
    INIT_OBJE_FILE();
}
function INIT_OBJE_MODA()
{
    $("#FormRegi\\:btonElim").confirmation({container: '#FormRegi'});
    $("#FormRegi\\:fech").initDatePick();
    $("#FormRegi\\:fech2").initDatePick();
    
    try{$("#FormRegiDocu\\:btonElimDocu").confirmation({container: '#FormRegiDocu'});}catch(err){}
     $("#FormSegu\\:fech").initDatePick();
    $("#FormSegu\\:fech2").initDatePick();
     $("#FormSegu\\:btonElim").confirmation({container: '#FormSegu'});
    $("#FormRegiSegu\\:fechFin").initDatePick();
    $("#FormRegiSegu\\:fechIni").initDatePick();
    $(".select").selectpicker();

    try{$("#FormRegiDocu\\:btonElimDocu").confirmation({container: '#FormRegiDocu'});}catch(err){}

    try{$("#FormRegiDocu\\:fech").initDatePick();}catch(err){}
    
}

function INIT_OBJE_FINA(tipo)
{
    if(tipo === 1)
    {
       $("#TablDetalle").initBootTable();
    }
}