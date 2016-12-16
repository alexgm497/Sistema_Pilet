$(document).ready(function() {
    $.fn.initBootTable = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {            
            if(this.id === "TablRegi" || $(this).id === "TablRegi"){
            consObje([{name : 'codiObjePara', value : row.id.trim()}]); 
            }            
            if(this.id === "TablDona" || $(this).id === "TablDona"){
            consDonaObje([{name : 'codiObjePara', value : row.idDona.trim()}]); 
            }
            if(this.id === "TablSegu" || $(this).id === "TablSegu"){
            consSeguObje([{name : 'codiObjePara', value : row.idSegu.trim()}]); 
            }
            if(this.id === "TablRegiHist" || $(this).id === "TablRegiHist"){
            consObjeDocu([{name : 'codiObjePara', value : row.idDocu.trim()}]); 
            }
            if(this.id === "TablDocu" || $(this).id === "TablDocu"){
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
       $("#TablRegi").bootstrapTable('uncheckAll');
    });
    $('#ModaForm').on('hide.bs.modal', function() {
        $("#TablRegi").bootstrapTable('uncheckAll');
        INIT_OBJE();
        console.log("Cierro donaciones");
    });
    $('#ModaDonaForm').on('show.bs.modal', function() {
       $("#TablDona").bootstrapTable('uncheckAll');
    });
    $('#ModaDonaForm').on('hide.bs.modal', function() {
        $("#TablDona").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro donaciones");
    });
    $('#ModaSeguForm').on('show.bs.modal', function() {
       console.log("asafads");
       $("#TablSegu").bootstrapTable('uncheckAll');
    });
    $('#ModaSeguForm').on('hide.bs.modal', function() {
        $("#TablSegu").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro donaciones");
    });
    INIT_OBJE();
});

function INIT_OBJE()
{
    try{$("#TablDona").initBootTable();}catch(err){}
    try{$("#TablSegu").initBootTable();}catch(err){}
    try{$("#TablDocu").initBootTable();}catch(err){}
    try{$("#TablRegi").initBootTable();}catch(err){}
    try{$(".select").selectpicker();}catch(err){}
    INIT_OBJE_MODA();
    INIT_OBJE_FILE();
    
}

function INIT_OBJE_DEMO()
{
    alert("ok");    
}
function INIT_OBJE_MODA()
{
    try{$("#FormRegi\\:btonElim2").confirmation({container: '#FormRegi'});}catch(err){}
    try{$("#FormDona\\:btonElim").confirmation({container: '#FormDona'});}catch(err){}
    try{$("#FormSegu\\:btonElim").confirmation({container: '#FormSegu'}); }catch(err){}
    try{$("#FormRegiDocu\\:btonElimDocu").confirmation({container: '#FormRegiDocu'});}catch(err){}
    try{$("#FormEmpr\\:fech").initDatePick();}catch(err){}
    try{$("#FormRegiDocu\\:fech").initDatePick();}catch(err){}
    try{$("#FormRegi\\:fech2").initDatePick();}catch(err){}
    try{$("#FormSegu\\:fech").initDatePick();}catch(err){}
    try{$("#FormSegu\\:fech2").initDatePick();}catch(err){}
    $(".select").selectpicker();
}
