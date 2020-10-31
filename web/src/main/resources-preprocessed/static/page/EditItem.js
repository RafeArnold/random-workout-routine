import React from "react";
import update from "immutability-helper";
import {Redirect} from "react-router-dom";
import {editPath} from "../util/apiUtils";

// TODO: Show a 404 screen if the item doesn't exist.
class EditItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {item: null, tags: "", redirectToEdit: false, deleting: false};
        this.emptyItem = {name: "", tags: []}
        this.setItem = this.setItem.bind(this);
        this.updateName = this.updateName.bind(this);
        this.updateTags = this.updateTags.bind(this);
        this.handleTagsInputChange = this.handleTagsInputChange.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.delete = this.delete.bind(this);
        this.redirect = this.redirect.bind(this);
    }

    componentDidMount() {
        const id = this.props.match.params.id;
        if (id) {
            this.getItem(id, this.setItem);
        } else {
            this.setState({item: this.emptyItem});
        }
    }

    setItem(item) {
        this.setState({item: item});
        this.updateTags(item?.tags?.join(", ") || "");
    }

    updateName(event) {
        const name = event.target.value;
        this.setState(state => {
            return {item: update(state.item, {name: {$set: name}})};
        });
    }

    updateTags(tags) {
        this.setState({tags: tags});
    }

    handleTagsInputChange(event) {
        const tags = event.target.value;
        this.updateTags(tags);
    }

    handleFormSubmit(event) {
        event.preventDefault();
        const item = this.state.item;
        item.tags = this.state.tags.split(",");
        this.saveItem(item, this.redirect);
    }

    delete() {
        this.deleteItem(this.state.item.id, this.redirect);
    }

    redirect() {
        this.setState({redirectToEdit: true});
    }

    render() {
        if (this.state.redirectToEdit) {
            return <Redirect to={editPath}/>;
        }
        const item = this.state.item;
        return (
            <>
                <div className="row justify-content-between align-items-center">
                    <div className="col">
                        <h1>Edit {this.itemDisplayName + " " + item?.name}</h1>
                    </div>
                    {item?.id ?
                        <div className="col-auto">
                            {this.state.deleting ?
                                <>
                                    <span>Are you sure?</span>
                                    <button className="btn btn-danger mx-3" onClick={this.delete}>Yes</button>
                                    <button className="btn btn-dark"
                                            onClick={() => this.setState({deleting: false})}>No
                                    </button>
                                </> :
                                <button className="btn btn-danger"
                                        onClick={() => this.setState({deleting: true})}>Delete</button>
                            }
                        </div> : null}
                </div>
                {item ?
                    <form onSubmit={this.handleFormSubmit}>
                        <input name="id" value={item.id} readOnly hidden/>
                        <div className="form-group">
                            <label htmlFor="name">Name</label>
                            <input className="form-control" id="name" name="name" value={item.name}
                                   onChange={this.updateName}/>
                        </div>
                        {this.extraInputs()}
                        <div className="form-group">
                            <label htmlFor="tags">Tags <small>(separate with commas)</small></label>
                            <input className="form-control" id="tags" name="tags"
                                   value={this.state.tags} onChange={this.handleTagsInputChange}/>
                        </div>
                        <div className="d-flex justify-content-between mb-3">
                            <button type="submit" className="btn btn-dark">Save</button>
                            <button type="button" className="btn btn-outline-dark"
                                    onClick={this.redirect}>Cancel
                            </button>
                        </div>
                    </form>
                    : null}
            </>
        );
    }
}

export default EditItem;