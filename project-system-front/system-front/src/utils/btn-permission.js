import store from "@/store";

/**
 * Determine if the current user has permission to use this button
 * @param {Button permission string} permission
 */
export default function hasBtnPermission(permission) {
  // Get all button permissions for the current user
  const myBtns = store.getters.buttons;
  // If the specified function permission is in myBtns, return true ==> the button will be shown, otherwise it will be hidden
  return myBtns.indexOf(permission) !== -1;
}
