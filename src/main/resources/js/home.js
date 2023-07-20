$(document).ready(function() {
    // Associa il click all'elemento .flip-card-inner presente al caricamento iniziale e dinamicamente
    $(document).on("click", ".flip-card-inner", function() {
        $(this).toggleClass("rotate"); // Applica l'effetto flip card
    });

    // Chiudi la modale quando viene nascosta
    $(".modal").on("hidden.bs.modal", function() {
        $(this).find(".flip-card-inner").removeClass("rotate"); // Rimuovi l'effetto flip card quando la modale viene chiusa
    });
});
$(document).ready(function() {
    // Aggiungi la classe 'flip-card' quando la modale viene aperta
    $('.flip-card-trigger').on('show.bs.modal', function() {
        $(this).find('.modal-dialog').addClass('flip-card');
    });

    // Rimuovi la classe 'flip-card' quando la modale viene chiusa
    $('.flip-card-trigger').on('hide.bs.modal', function() {
        $(this).find('.modal-dialog').removeClass('flip-card');
    });
});