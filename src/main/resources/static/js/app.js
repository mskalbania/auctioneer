$(document).ready(function () {

    const USERS = "/api/v1/user";
    const ITEM = "/api/v1/item"

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
    })

    $("#add_item_btn").click(function () {
        let name = $("#add_item_form_name");
        let owner = $("#add_item_form_owner");
        let price = $("#add_item_form_initial");
        $.ajax({
            url: ITEM,
            method: "post",
            dataType: "text",
            contentType: "application/json",
            data: JSON.stringify({
                name: name.val(),
                ownerId: owner.val(),
                initialPrice: price.val()
            })
        }).done(function (rs) {
            $("#item_id").text(rs)
            $("#item_add_error").text('')
        }).fail(function (rs) {
            let jsonObject = JSON.parse(rs.responseText)
            $("#item_add_error").text(jsonObject.message)
            $("#item_id").text('')
        })
        name.val('')
        owner.val('')
        price.val('')
    })

    $("#search_item_btn").click(function () {
        let textElement = $("#search_item_form");
        $.ajax({
            url: `${ITEM}/${textElement.val()}`,
            method: "get",
            dataType: "json",
            contentType: "application/json",
        }).done(function (rs) {
            $("#find_item_success").empty()
            appendItemRs(rs)
            $("#find_item_error").text('')
        }).fail(function (rs) {
            $("#find_item_error").text(rs.responseJSON.message)
            $("#find_success").empty()
        })
    })

    $("#find_item_success").on('click', '#bid_item_btn', function (event) {
        let rendered_item_id = $("#rendered_item_id");
        let bidderId = $("#bid_item_bidderId");
        let bidAmount = $("#bid_item_amount");
        $.ajax({
            url: `${ITEM}/${rendered_item_id.text()}/bid`,
            method: "post",
            dataType: "text",
            contentType: "application/json",
            data: JSON.stringify({
                bidderId: bidderId.val(),
                amount: bidAmount.val()
            })
        }).done(function () {
            $("#item_bid_error").text('')
        }).fail(function (rs) {
            let jsonObject = JSON.parse(rs.responseText)
            $("#item_bid_error").text(jsonObject.message)
        })
        bidAmount.empty()
    })

    $("#search_items").keyup(function (event) {
            let searchQuery = event.target.value;
            if (searchQuery.length > 2) {
                $.ajax({
                    url: `${ITEM}?like=${searchQuery}`,
                    method: "get",
                    dataType: "json",
                    contentType: "application/json",
                }).done(function (rs) {
                    console.log(rs)
                    appendItemsRs(rs)
                })
            }
        }
    );

    function appendItemsRs(rs) {
        let hook = $("#search_items_result")
        hook.empty()
        if (rs.length === 0) {
            return
        }
        hook.append(`<table class="table">
                    <thead>
                      <tr>
                        <th scope="col">Item id</th>
                        <th scope="col">Name</th>
                        <th scope="col">Initial price</th>
                      </tr>
                    </thead>
                    <tbody>
                        ${rs.map(item =>`<tr>
                                            <td>${item.id}</td>
                                            <td>${item.name}</td>    
                                            <td>${item.initialPrice}</td>   
                                        </tr>`).join('')}
                    </tbody>
                    </table>`)
    }

    function appendItemRs(rs) {
        $("#find_item_success").append(`
            <div id="rendered_item_id" style="display: none">${rs.id}</div>
            <div class="row"><h5>Name: ${rs.name}</h5></div>
            <div class="row"><h5>Owner ID: ${rs.ownerId}</h5></div>
            <div class="row"><h5>Initial Price: ${rs.initialPrice}</div>
            <div class="row"><h5>Highest Bid: ${rs.highestBid !== undefined ? rs.highestBid : 'NONE'}</div>
            <div class="row"><h5>Item Bids: </div>
            <div class="row">
                <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">Bidder id</th>
                        <th scope="col">Created on</th>
                        <th scope="col">Amount</th>
                      </tr>
                    </thead>
                    <tbody>
                        ${bids(rs.bids)}
                      </tr>
                    </tbody>
                </table>
            </div>
            <div class="row"><h5>Bid Item: </div>
            <div class="row text-center">
                <form onsubmit="return false">
                    <div class="form-row align-items-center">
                        <div class="col-auto">
                            <input type="text" class="form-control mb-2" id="bid_item_amount" placeholder="Enter Amount">
                        </div>
                        <div class="col-auto">
                            <input type="text" class="form-control mb-2" id="bid_item_bidderId" placeholder="Enter Bidder id">
                        </div>
                        <div class="col-auto">
                            <button type="submit" id="bid_item_btn" class="btn btn-primary mb-2">Submit</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row">
                <div class="col-2"><h4>Error: </h4></div>
                <div class="col-10"><h5 id="item_bid_error"></h5></div>
            </div>
            </div>
        `)
    }

    function appendUserRs(rs) {
        $("#find_success").append(`
            <div class="row"><h5>User ID: ${rs.id}</h5></div>
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
               ${bid.targetItemId !== undefined ? `<td>${bid.targetItemId}</td>` : ``}
               ${bid.bidderId !== undefined ? `<td>${bid.bidderId}</td>` : ``}    
               <td>${bid.createdOn}</td>   
               <td>${bid.amount}</td>
            </tr>`).join('')
        }
        return ''
    }
});