import { Link, LinkProps, RelativeRoutingType } from "react-router-dom";
import useStyles from "./Header.styles";
interface NavItemProps extends LinkProps {
  label: string;
  to: string;
  relative?: RelativeRoutingType;
}
export function NavItem({ label, to, relative, hidden }: NavItemProps) {
  const { classes } = useStyles();
  return (
    <Link
      key={label}
      to={to}
      className={classes.link}
      relative={relative ?? "route"}
      hidden={hidden}
    >
      {label}
    </Link>
  );
}
