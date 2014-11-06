/** Adding and removing poll options */
var POLL = {
    choiceCount: 0,
    /**
     * Add option
     * @param container
     * @param value
     */
    addChoice: function (container, value) {
        value = value || "";
        container.append('<div class="col-sm-4 choice-box">'
            + '<div class="input-group">'
            + '<input name="choiceStringList[' + (this.choiceCount++) + ']" value="' + value + '" class="choice form-control" />'
            + '<span class="input-group-addon"><span class="glyphicon glyphicon-minus-sign" onclick="POLL.removeLastChoice(this); return false;" style="cursor: pointer;" title="Remove option"></span></span></div></div>');
    },
    addPredefinedOptions: function (url) {
        $.getJSON(url, function (data) {
            $("#choices").empty();
            for (var i = 0; i < data.length; i++) {
                POLL.addChoice($("#choices"), data[i]);
            }
        });
        return false;
    },
    /**
     * remove last option
     * @param container
     */
    removeLastChoice: function (parent) {
        $(parent).parent().parent().parent().remove();
    }

};