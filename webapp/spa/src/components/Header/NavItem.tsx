import { Link, LinkProps, RelativeRoutingType } from "react-router-dom";
import useStyles from "./Header.styles";
import { useRestaurantFilterAndPage } from "@/context/RestaurantFilterAndPageContext";
interface NavItemProps extends LinkProps {
  label: string;
  to: string;
  relative?: RelativeRoutingType;
}
export function NavItem({ label, to, relative, hidden }: NavItemProps) {
  const { classes } = useStyles();
  const { setPageParams, setFilterParams } = useRestaurantFilterAndPage();
  const clearFilters = () => {
    setPageParams();
    setFilterParams({});
  };

  return (
    <Link
      key={label}
      to={to}
      className={classes.link}
      relative={relative ?? "route"}
      hidden={hidden}
      onClick={clearFilters}
    >
      {label}
    </Link>
  );
}
