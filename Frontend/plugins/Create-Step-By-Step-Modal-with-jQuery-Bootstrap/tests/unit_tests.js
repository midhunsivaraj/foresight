/* global QUnit */
/* global $ */

QUnit.test('btnOptions', function(assert){
    /* Check if default modalSteps and buttons customize modalSteps instance is ok */
    var $modal = $('#modal-steps'),
        $btnCancel = $modal.find('.js-btn-step[data-orientation=cancel]'),
        $btnPrevious = $modal.find('.js-btn-step[data-orientation=previous]'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]'),
        expectedCancel = 'Cancelar',
        expectedPrevious = 'Voltar',
        expectedNext = 'Proximo';

    assert.equal($btnCancel.html(), '');
    assert.equal($btnPrevious.html(), '');
    assert.equal($btnNext.html(), '');

    $modal.modalSteps({
        btnCancelHtml: expectedCancel,
        btnPreviousHtml: expectedPrevious,
        btnNextHtml: expectedNext
    });
    $modal.modal('show');

    assert.equal($btnCancel.html(), expectedCancel);
    assert.equal($btnPrevious.html(), expectedPrevious);
    assert.equal($btnNext.html(), expectedNext);
});

QUnit.asyncTest('btnCancel', function(assert){
    /* Check if cancel button when triggered reset steps and buttons */
    var $modal = $('#modal-steps'),
        $btnCancel = $modal.find('.js-btn-step[data-orientation=cancel]'),
        $btnPrevious = $modal.find('.js-btn-step[data-orientation=previous]'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]'),
        $actualStep;

    $modal.modalSteps();
    $modal.modal('show');
    $actualStep = $modal.find('#actual-step');

    assert.equal($actualStep.val(), '1');

    $btnNext.trigger('click');

    assert.equal($btnPrevious.attr('data-step'), '1');
    assert.equal($actualStep.val(), '2');

    $btnCancel.trigger('click');

    // TODO: Next step must be 3, right?
    // assert.equal($nextStep.val(), '3');

    $modal.on('hidden.bs.modal', function(){
        assert.equal($btnNext.attr('data-step'), '1');
        assert.equal($modal.find('#actual-step').length, 0); 

        QUnit.start();
    });
});

QUnit.test('btnPrevious', function(assert){
    var $modal = $('#modal-steps'),
        $btnPrevious = $modal.find('.js-btn-step[data-orientation=previous]'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]'),
        $actualStep;

    $modal.modalSteps();
    $modal.modal('show');
    $actualStep = $modal.find('#actual-step');

    // For the first step, previous button must be disabled
    assert.equal($btnPrevious.attr('disabled'), 'disabled');

    // Go to the next step
    $btnNext.trigger('click');

    // Previous button must be active and with data-step correct
    assert.equal($btnPrevious.attr('disabled'), undefined);
    assert.equal($btnPrevious.attr('data-step'), $actualStep.val() - 1);

    // Previous button clicked and modal go back to the first step
    $btnPrevious.trigger('click');
    assert.equal($btnPrevious.attr('disabled'), 'disabled');
    assert.equal($btnPrevious.attr('data-step'), $actualStep.val() - 1);
});

QUnit.test('btnNext', function(assert){
    var $modal = $('#modal-steps'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]'),
        $actualStep;

    $modal.modalSteps();
    $modal.modal('show');
    $actualStep = $modal.find('#actual-step');

    assert.equal($btnNext.attr('data-step'), '2');

    $btnNext.trigger('click');
    assert.equal($btnNext.attr('data-step'), '2'); // TODO: must be 3, right?
});

QUnit.test('btnLastStep', function(assert){
    var $modal = $('#modal-steps'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]');

    $modal.modalSteps();
    $modal.modal('show');

    var steps = $modal.find('div[data-step]').length;

    for (var i=1; i < steps; i++){
        $btnNext.trigger('click');
    }

    assert.equal($btnNext.attr('data-step'), 'complete');
});

QUnit.test('callbackOption', function(assert){
    var $modal = $('#modal-steps'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]');

    var callback = function(){
        $modal.find('#testCallback').html('2');
    };

    $modal.modalSteps({
        completeCallback: callback
    });

    $modal.modal('show');

    var steps = $modal.find('div[data-step]').length;

    for (var i=1; i < steps; i++){
        $btnNext.trigger('click');
    }

    assert.equal($modal.find('#testCallback').html(), '1');
    $btnNext.trigger('click'); // Complete steps
    assert.equal($modal.find('#testCallback').html(), '2');
});

QUnit.test('checkTitle', function(assert){
    var $modal = $('#modal-steps'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]'),
        steps = $modal.find('div[data-step]').length,
        testString,
        $step;

    $modal.modalSteps();
    $modal.modal('show');

    for (var step=1; step <= steps; step++){
        $step = $modal.find('[data-step=' + step + ']');

        testString = step + ' ' + $step.attr('data-title');
        assert.equal(testString, $modal.find('.js-title-step').text());

        $btnNext.trigger('click');
    }
});

QUnit.test('chaining', function(assert){
    var $modal = $('#modal-steps'),
        steps = $modal.find('div[data-step]').length;
    assert.equal($modal.modalSteps().find('div[data-step]').length, steps);
});

QUnit.test('everyStepCallback', function(assert){
    /*
     * A callback is executed when the modal shows up and when
     * a new step appeared.
     */
    var $modal = $('#modal-steps'),
        $btnPrevious = $modal.find('.js-btn-step[data-orientation=previous]'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]'),
        steps = $modal.find('div[data-step]').length,
        stepForward = 1,
        callback;

    callback = function(){
        var valor = parseInt($('#testCallback').html()) + 1;
        $('#testCallback').html(valor);
    };

    $modal.modalSteps({
        callbacks: {
            '*': callback
        }
    }).modal('show');

    for (var step=1; step < steps; step++){
        stepForward += 1;

        assert.equal($('#testCallback').html(), stepForward);
        $btnNext.trigger('click');
        assert.equal($('#testCallback').html(), stepForward + 1);
    }

    for (step=stepForward; step > 1; step--){
        $btnPrevious.trigger('click');
        stepForward += 1;
        assert.equal($('#testCallback').html(), stepForward + 1);
    }
});

QUnit.test('stepCallback', function(assert){
    var $modal = $('#modal-steps'),
        $btnPrevious = $modal.find('.js-btn-step[data-orientation=previous]'),
        $btnNext = $modal.find('.js-btn-step[data-orientation=next]'),
        callback;

    callback = function(){
        $('#testCallback').html('callback 2');
    };

    $modal.modalSteps({
        callbacks: {
            '1': function(){ $('#testCallback').html('callback 1'); },
            '2': function(){ $('#testCallback').html('callback 2'); },
            '3': function(){ $('#testCallback').html('callback 3'); }
        }
    }).modal('show');

    $btnNext.trigger('click');
    assert.equal($('#testCallback').html(), 'callback 2');

    $btnPrevious.trigger('click');
    assert.equal($('#testCallback').html(), 'callback 1');
});

QUnit.test('invalidStepCallback', function(assert){
    var $modal = $('#modal-steps');

    assert.throws(
        function(){
            $modal.modalSteps({
                'callbacks': { '1': 'error' }
            }).modal('show');
        },
        'Step 1 callback must be a function'
    );
});

QUnit.test('invalidEveryStepCallback', function(assert){
    var $modal = $('#modal-steps');

    assert.throws(
        function(){
            $modal.modalSteps({
                'callbacks': { '*': 'error' }
            }).modal('show');
        },
        'Every step callback must be a function'
    );
});
