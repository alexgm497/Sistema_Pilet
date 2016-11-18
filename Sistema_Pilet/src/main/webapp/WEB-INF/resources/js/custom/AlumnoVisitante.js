$(document).ready(function() {
    $.fn.initBootTableAlum = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeAlum([{name : 'codiObjeAlum', value : row.id.trim()}]);
        });
        return false;
    };
    $.fn.initBootTableAlumNoDoce = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeAlum([{name : 'codiObjeAlum', value : row.id.trim()}]);
        });
        return false;
    };
    INIT_FORM();
});

function otroParen(val){
    if(val  === "Otro"){
        document.getElementById("ParenDiv").setAttribute("style","display:box;");
    }else{
        document.getElementById("ParenDiv").setAttribute("style","display:none;");
       $("#FormRegi\\:pareEven").val("");
    }
}
function selectedItem(val){
    $("#FormRegi\\:seleParen option:eq("+val+")").prop('selected', true);
}

function INIT_FORM(){
    $("#FormRegi\\:seleParen").change(function() {
        otroParen(this.options[this.selectedIndex].innerHTML);
      });
      var select = document.getElementById("FormRegi:seleParen");
      otroParen(select[select.selectedIndex].innerHTML);
}

function INIT_OBJE_TABL(){
    $("#TablAlum").initBootTableAlum();
    $("#TablAlumNoDoce").initBootTableAlumNoDoce();
}