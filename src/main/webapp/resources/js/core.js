/**
 * Created by Devid on 2016/10/13.
 */
(function(w){
    var modalRemoteHandler = function ($this, e) {
        e.preventDefault();
        var url = $this.attr('href') || $this.data('url');
        $.get(url, function (data) {
            $('div.modal').html(data).modal('show');
        });
    };
    $('[data-toggle="modal-remote"]').on('click', function (e) {
        modalRemoteHandler($(this), e);
    });
    $('[data-toggle="modal-remote-dbclick"]').on('dbclick', function (e) {
        modalRemoteHandler($(this), e);
    });
})(Window);