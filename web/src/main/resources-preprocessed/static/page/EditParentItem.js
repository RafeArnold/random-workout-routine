import React from "react";
import EditItem from "./EditItem";
import update from "immutability-helper";

class EditParentItem extends EditItem {
    constructor(props) {
        super(props);
        this.state.searchResults = null;
        this.state.searchInputValue = "";
        this.addChildItem = this.addChildItem.bind(this);
        this.removeChildItem = this.removeChildItem.bind(this);
        this.handleSearchInputChange = this.handleSearchInputChange.bind(this);
        this.searchChildItem = this.searchChildItem.bind(this);
        this.setSearchResults = this.setSearchResults.bind(this);
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.searchInputValue !== prevState.searchInputValue ||
            this.state.item?.[this.childItemPropertyName]?.length !== prevState.item?.[this.childItemPropertyName]?.length) {
            this.searchChildItem(this.state.searchInputValue);
        }
    }

    addChildItem(childItemId) {
        this.setState(state => {
            if (state.item?.[this.childItemPropertyName]?.filter(childItem => childItem.id === childItemId).length === 0) {
                const childItem = state.searchResults.filter(childItem => childItem.id === childItemId);
                return {item: update(state.item, {[this.childItemPropertyName]: {$push: childItem}})};
            }
        });
    }

    removeChildItem(childItemId) {
        this.setState(state => {
            const childItems = state.item?.[this.childItemPropertyName];
            const filteredChildItems = childItems?.filter(childItem => childItem.id === childItemId);
            if (filteredChildItems.length !== 0) {
                const childItemIndex = childItems?.indexOf(filteredChildItems[0]);
                return {item: update(state.item, {[this.childItemPropertyName]: {$splice: [[childItemIndex, 1]]}})};
            }
        });
    }

    handleSearchInputChange(event) {
        const searchTerm = event.target.value;
        this.setState({searchInputValue: searchTerm});
    }

    searchChildItem(searchTerm) {
        const filter = {
            searchTerm: searchTerm,
            excludedTerms: this.state.item?.[this.childItemPropertyName]?.map(childItem => childItem.name)
        };
        this.searchChildItemNames(filter, this.setSearchResults);
    }

    setSearchResults(childItems) {
        this.setState({searchResults: childItems});
    }

    extraInputs() {
        const childListItems = this.state.item?.[this.childItemPropertyName]?.map(child =>
            <li key={child.id} className="list-group-item d-flex align-items-center justify-content-between">
                <strong>{child.name}</strong>
                <button type="button" onClick={() => this.removeChildItem(child.id)} className="btn btn-danger">
                    <i className="oi oi-trash"/>
                </button>
            </li>);
        const searchResultsListItems = this.state.searchResults?.map(searchResult =>
            <li key={searchResult.id} onClick={() => this.addChildItem(searchResult.id)}
                className="list-group-item list-group-item-action">{searchResult.name}</li>);
        return (
            <div className="row">
                <div className="col-lg-7 form-group">
                    <label htmlFor={this.childItemPropertyName}>{this.childItemDisplayNamePlural}</label>
                    <ul id={this.childItemPropertyName} className="list-group">
                        {childListItems}
                    </ul>
                </div>
                <div className="col-lg form-group">
                    <label htmlFor={this.childItemPropertyName + "-search"}><small>Search</small></label>
                    <input className="form-control" id={this.childItemPropertyName + "-search"}
                           value={this.state.searchInputValue}
                           onChange={this.handleSearchInputChange}/>
                    <ul className="list-group mt-3">{searchResultsListItems}</ul>
                </div>
            </div>
        );
    }
}

export default EditParentItem;