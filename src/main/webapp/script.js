$(document).ready(function () {
    $.ajax({
        type: 'GET',
        crossDomain: true,
        url: 'http://localhost:8080/cinema/hall',
        dataType: 'text',
        success: funcSuccess
    })
})

function sendData() {
    let send = $('#form').val()
    const temp = $('#form').serialize()
    const str = JSON.stringify(temp)
    console.log(send)
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/cinema/hall',
        crossDomain: true,
        dataType: 'text',
        data: ({norm: temp, str: str}),
        success: function (data) {
            console.log(data)
            document.getElementById('info').innerText = data
            pay(data)
        }
    })
}

function funcSuccess(data) {
    const temp = JSON.parse(data)
    let table = $('#table')
    let r = 0
    for (let item of temp) {
        if (r !== item.row || r === 0) {
            r = item.row
            table.append('<tr>')
        }

        let td = '<td ' + occupied(item.accountId) + '> Ряд '
            + item.row + ' Место' + item.column + ' Цена ' + item.price
            + '<input name="' + item.id + '" type="checkbox" ' + isChecked(item.accountId) + '>'
            + '</td>'
        table.append(td)

        if (item.column === 10) {
            table.append('</tr>')
        }
    }
}

function isChecked(data) {
    if (data !== 0) {
        return 'hidden';
    }
}

function occupied(data) {
    if (data === 0) {
        return ' style="background-color: lightgreen"'
    } else {
        return ' style="background-color: red"'
    }
}


function pay(data) {
    sessionStorage.setItem("seats", data)
    window.location.href = '/cinema/payment.html'
}

