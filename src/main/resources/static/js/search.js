var searchManager = (function () {
    var get = function (obj, callback) {
        $.ajax({
            type: 'get',
            url: '/course/' + obj.year + '/' + obj.semester + '/' + obj.code,
            success: callback
        });
    };

    var post = function (obj, callback) {
        $.ajax({
            type: 'post',
            url: '/course/create',
            data: {
                year: obj.year,
                semester: obj.semester,
                grade: obj.grade,
                major: obj.major,
                credit: obj.credit,
                holiday: obj.holiday,
                majorcnt: obj.majorcnt,
                general: obj.general
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(obj.header, obj.token);
            },
            success: callback
        })
    };

    return {
        get: get,
        post: post
    }
})();