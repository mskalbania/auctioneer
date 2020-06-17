$(document).ready(function () {

    const USERS = "/api/v1/user";

    $("#reg_user_btn").click(function () {
        let textElement = $("#reg_user_form");
        $.ajax({
            url: USERS,
            method: "post",
            dataType: "text",
            contentType: "application/json",
            data: JSON.stringify({
                name: textElement.val()
            })
        }).done(function (rs) {
            $("#token").text(rs)
            $("#register_error").text('')
        }).fail(function (rs) {
            let jsonObject = JSON.parse(rs.responseText)
            $("#register_error").text(jsonObject.message)
            $("#token").text('')
        })
        textElement.val('')
    })

    $("#search_user_btn").click(function () {
        let textElement = $("#search_user_form");
        $.ajax({
            url: `${USERS}/${textElement.val()}`,
            method: "get",
            dataType: "json",
            contentType: "application/json",
        }).done(function (rs) {
            $("#find_success").empty()
            appendUserRs(rs)
            $("#find_error").text('')
        }).fail(function (rs) {
            $("#find_error").text(rs.responseJSON.message)
            $("#find_success").empty()
        })
        textElement.val('')
    })

    function appendUserRs(rs) {
        $("#find_success").append(`
            <div class="row"><h5>ID: ${rs.id}</h5></div>
            <div class="row"><h5>Name: ${rs.name}</h5></div>
            <div class="row"><h5>My Items: </div>
            <div class="row">
                <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">Name</th>
                        <th scope="col">Initial price</th>
                        <th scope="col">Highest bid</th>
                      </tr>
                    </thead>
                    <tbody>
                        ${items(rs.items)}
                      </tr>
                    </tbody>
                </table>
            </div>
            <div class="row"><h5>My Bids: </div>
            <div class="row">
                <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">Target item</th>
                        <th scope="col">Created on</th>
                        <th scope="col">Amount</th>
                      </tr>
                    </thead>
                    <tbody>
                        ${bids(rs.bids)}
                    </tbody>
                </table>
            </div>
        `)
    }

    function items(items) {
        if (items !== undefined) {
            return items.map(item =>
                `<tr>
               <td>${item.name}</td>    
               <td>${item.initialPrice}</td>   
               <td>${item.highestBid !== undefined ? item.highestBid : 'NONE'}</td>
            </tr>`).join('')
        }
        return ''
    }

    function bids(bids) {
        if (bids !== undefined) {
            return bids.map(bid =>
                `<tr>
               <td>${bid.targetItemId}</td>    
               <td>${bid.createdOn}</td>   
               <td>${bid.amount}</td>
            </tr>`).join('')
        }
        return ''
    }
});